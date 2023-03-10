package com.hk.im.service.worker;

import com.hk.im.client.service.MinioService;
import com.hk.im.client.service.SequenceService;
import com.hk.im.common.consntant.MinioConstant;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.FileMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.message.chat.AttachmentMessage;
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
import java.io.File;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : AttachmentMessageWorker
 * @date : 2023/3/10 15:12
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class AttachmentMessageWorker {

    @Resource
    private MinioService minioService;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private BaseMessageWorker baseMessageWorker;
    @Resource
    private ApplicationContext applicationContext;


    public ResponseResult sendMessage(AttachmentMessage request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getReceiverId()) || Objects.isNull(request.getTalkType())
                || Objects.isNull(request.getFile());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("附件消息参数错误!");
        }

        // 校验消息类型
        Integer chatMessageType = request.getChatMessageType();
        if (MessageConstants.ChatMessageType.FILE.ordinal() != chatMessageType) {
            // 不是文件消息类型
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 素材准备
        Long senderId = Long.valueOf(request.getSenderId());
        if (Objects.isNull(request.getSenderId())) {
            senderId = UserContextHolder.get().getId();
        }
        Long receiverId = Long.valueOf(request.getReceiverId());
        MultipartFile file = request.getFile();
        Integer talkType = request.getTalkType();

        // 上传图片
        String fileUrl = this.minioService.putChatFile(file, MinioConstant.BucketEnum.File.getBucket(), senderId);
        // 图片消息扩展信息
        FileMessageExtra fileMessageExtra = this.calculateExtra(file);
        fileMessageExtra.setUploader(senderId).setReceiver(receiverId);
        // 保存消息
        ChatMessage chatMessage = new ChatMessage()
                // 消息内容
                .setContent(fileUrl)
                .setUrl(fileUrl)
                // 消息特性
                .setMessageFeature(MessageConstants.MessageFeature.DEFAULT.ordinal())
                // 消息类型
                .setMessageType(MessageConstants.ChatMessageType.FILE.ordinal())
                .setExtra(fileMessageExtra);

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
     * 计算文件属性
     *
     * @param file
     *
     * @return
     */
    private FileMessageExtra calculateExtra(MultipartFile file) {

        FileMessageExtra extra = new FileMessageExtra();
        extra.setFileName(file.getOriginalFilename());
        extra.setOriginalFileName(file.getOriginalFilename());
        extra.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
        extra.setSize((double) file.getSize() / 1048576);
        extra.setFileSubType(MessageConstants.FileSubType.FILE.ordinal());

        return extra;
    }


}
