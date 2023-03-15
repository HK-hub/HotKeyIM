package com.hk.im.service.worker;

import com.hk.im.client.service.*;
import com.hk.im.common.consntant.MinioConstant;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.BaseMessageExtra;
import com.hk.im.domain.dto.ImageMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.message.chat.ImageMessage;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : ImageMessageWorker
 * @date : 2023/3/9 22:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class ImageMessageWorker {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private MinioService minioService;

    @Resource
    private BaseMessageWorker baseMessageWorker;

    public ResponseResult sendMessage(ImageMessage request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getReceiverId()) || Objects.isNull(request.getTalkType())
                || Objects.isNull(request.getImage());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("图片消息参数错误!");
        }

        // 校验消息类型
        Integer chatMessageType = request.getChatMessageType();
        if (MessageConstants.ChatMessageType.IMAGE.ordinal() != chatMessageType) {
            // 不是文本消息类型
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 素材准备
        Long senderId = Long.valueOf(request.getSenderId());
        if (Objects.isNull(request.getSenderId())) {
            senderId = UserContextHolder.get().getId();
        }
        Long receiverId = Long.valueOf(request.getReceiverId());
        MultipartFile image = request.getImage();
        Integer talkType = request.getTalkType();

        // 上传图片
        String imageUrl = this.minioService.putChatImage(image, MinioConstant.BucketEnum.Image.getBucket(), senderId);
        // 图片消息扩展信息
        ImageMessageExtra imageMessageExtra = this.calculateExtra(image);
        imageMessageExtra.setUploader(senderId).setReceiver(receiverId);
        // 保存消息
        ChatMessage chatMessage = new ChatMessage()
                // 消息内容
                .setContent(imageUrl)
                .setUrl(imageUrl)
                // 消息特性
                .setMessageFeature(MessageConstants.MessageFeature.DEFAULT.ordinal())
                // 消息类型
                .setMessageType(MessageConstants.ChatMessageType.IMAGE.ordinal())
                .setExtra(imageMessageExtra);

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
     * 装配出图片消息扩展信息
     *
     * @param image
     *
     * @return
     */
    private ImageMessageExtra calculateExtra(MultipartFile image) {

        ImageMessageExtra extra = new ImageMessageExtra();

        extra.setExtension(FilenameUtils.getExtension(image.getOriginalFilename()));
        extra.setFileName(image.getOriginalFilename())
                .setOriginalFileName(image.getOriginalFilename())
                .setFileSubType(MessageConstants.FileSubType.IMAGE.ordinal())
                // 文件大小单位：KB
                .setSize((double) image.getSize() / 1024);

        return extra;
    }


}
