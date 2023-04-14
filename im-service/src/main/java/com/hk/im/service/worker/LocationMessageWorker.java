package com.hk.im.service.worker;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hk.im.client.service.SequenceService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.LocationBO;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.dto.LocationMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.request.LocationMessageRequest;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author : HK意境
 * @ClassName : LocationMessageWorker
 * @date : 2023/4/13 21:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class LocationMessageWorker {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private BaseMessageWorker baseMessageWorker;


    /**
     * 发送地理位置消息
     * @param request
     * @return
     */
    public ResponseResult sendMessage(LocationMessageRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getSenderId()) || Objects.isNull(request.getLocation()) ||
                Objects.isNull(request.getReceiverId()) || Objects.isNull(request.getTalkType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 消息参数校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 校验消息类型
        Integer chatMessageType = request.getChatMessageType();
        if (MessageConstants.ChatMessageType.LOCATION.ordinal() != chatMessageType) {
            // 不是文本消息类型
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 素材准备
        Long senderId = request.getSenderId();
        Long receiverId = request.getReceiverId();
        LocationBO locationBO = request.getLocation();
        Integer talkType = request.getTalkType();

        // 扩展信息
        LocationMessageExtra extra = this.computedLocationExtra(locationBO);
        if (Objects.isNull(extra)) {
            // 获取位置失败：
            return ResponseResult.FAIL("获取地理位置失败!");
        }
        // 保存消息
        ChatMessage chatMessage = new ChatMessage()
                // 消息内容
                .setContent(extra.getAddress())
                // 消息特性
                .setMessageFeature(MessageConstants.MessageFeature.DEFAULT.ordinal())
                // 消息类型
                .setMessageType(MessageConstants.ChatMessageType.LOCATION.ordinal())
                .setExtra(extra);
        // 获取消息序列号
        ResponseResult sequenceResult = this.sequenceService.nextId(senderId, receiverId, talkType);
        if (BooleanUtils.isFalse(sequenceResult.isSuccess())) {
            // 获取序列号失败
            return ResponseResult.FAIL().setResultCode(ResultCode.SERVER_BUSY);
        }

        // 设置消息序列号
        chatMessage.setSequence((Long) sequenceResult.getData());
        // 消息流水
        MessageFlow messageFlow = new MessageFlow()
                .setSenderId(senderId).setReceiverId(receiverId)
                .setMessageType(chatMessage.getMessageType()).setChatType(talkType)
                .setSequence(chatMessage.getSequence())
                // 消息签收状态
                .setSignFlag(MessageConstants.SignStatsEnum.UNREAD.ordinal())
                // 消息发送状态
                .setSendStatus(MessageConstants.SendStatusEnum.SENDING.ordinal())
                .setDeleted(Boolean.FALSE);

        // 保存消息和消息流水
        MessageBO messageBO = this.baseMessageWorker.doSaveMessageAndFlow(chatMessage, messageFlow);
        // 判断消息发送是否成功
        if (Objects.isNull(messageBO)) {
            // 消息发送失败: 设置草稿->
            messageFlow.setSendStatus(MessageConstants.SendStatusEnum.FAIL.ordinal());
            messageBO = MessageMapStructure.INSTANCE.toBO(messageFlow, chatMessage);
            // TODO 发送消息保存事件
            this.applicationContext.publishEvent(new SendChatMessageEvent(this, messageBO));
            // 响应
            return ResponseResult.FAIL().setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 发送消息保存事件
        this.applicationContext.publishEvent(new SendChatMessageEvent(this, messageBO));

        // 发送消息成功
        return ResponseResult.SUCCESS(messageBO).setMessage("消息发送成功!");
    }


    /**
     * 计算当前位置信息
     * @param locationBO
     * @return
     */
    private LocationMessageExtra computedLocationExtra(LocationBO locationBO) {

        LocationMessageExtra extra = new LocationMessageExtra();
        // 设置经纬度
        extra.setLatitude(locationBO.getLatitude()).setLongitude(locationBO.getLongitude());

        try{
            // 查询位置信息: 逆地址编码查询
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("key", "fb8eaa292782a00000e337d4635c6df8");

            // 设置经纬度
            BigDecimal latitude = locationBO.getLatitude().setScale(6, RoundingMode.DOWN);
            BigDecimal longitude = locationBO.getLongitude().setScale(6, RoundingMode.DOWN);
            parameters.put("location", "" + longitude.toPlainString() + "," + latitude.toPlainString());
            // parameters.put("sig", "0cf0942b1590c335f9d05faa568e14a6");
            String jsonData = HttpUtil.get("https://restapi.amap.com/v3/geocode/regeo", parameters);
            JSONObject jsonObject = JSON.parseObject(jsonData);
            log.info("request location mapping address: {}", jsonObject);

            if (!"1".equals(jsonObject.get("status"))) {
                // 获取地址失败
                return null;
            }
            JSONObject regeocode = jsonObject.getJSONObject("regeocode");
            // 获取地址
            String address = regeocode.getString("formatted_address");
            extra.setAddress(address);

            // 获取地址组件
            JSONObject component = regeocode.getJSONObject("addressComponent");
            // 设置国家，省份,区县
            extra.setCountry(component.getString("country"))
                    .setProvince(component.getString("province"))
                    .setCity(component.get("city") == null ? "" : component.get("city").toString())
                    .setDistrict(component.getString("district"))
                    .setTowncode(component.getString("towncode"))
                    .setTownship(component.getString("township"));
            JSONObject streetNumber = component.getJSONObject("streetNumber");
            extra.setStreets(streetNumber.getString("street"))
                    .setNumber(streetNumber.getString("number"));
            return extra;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
