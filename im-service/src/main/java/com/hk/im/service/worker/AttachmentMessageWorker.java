package com.hk.im.service.worker;

import com.hk.im.client.service.MinioService;
import com.hk.im.client.service.SequenceService;
import com.hk.im.client.service.SplitUploadService;
import com.hk.im.common.consntant.MinioConstant;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.FileMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.CloudResource;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.message.chat.AttachmentMessage;
import com.hk.im.domain.request.MergeSplitFileRequest;
import com.hk.im.infrastructure.event.file.event.UploadFileEvent;
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
    @Resource
    private SplitUploadService splitUploadService;


    public ResponseResult sendMessage(AttachmentMessage request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getReceiverId()) || Objects.isNull(request.getTalkType())
                || StringUtils.isEmpty(request.getFileUploadId());
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
        Integer talkType = request.getTalkType();
        Long senderId = Long.valueOf(request.getSenderId());
        if (Objects.isNull(request.getSenderId())) {
            senderId = UserContextHolder.get().getId();
        }
        Long receiverId = Long.valueOf(request.getReceiverId());
        String hash = request.getFileUploadId();

        // 合并文件,上传文件
        ResponseResult result = this.mergeAndUploadFile(request);
        if (BooleanUtils.isFalse(result.isSuccess())) {
            // 文件合并或上传失败
            return ResponseResult.FAIL();
        }

        // 获取扩展信息
        FileMessageExtra extra = (FileMessageExtra) result.getData();

        // TODO 发布事件：保存云资源数据
        this.applicationContext.publishEvent(new UploadFileEvent(this, extra));
        // 保存消息
        ChatMessage chatMessage = new ChatMessage()
                // 消息内容
                .setContent(extra.getOriginalFileName())
                .setUrl(extra.getUrl())
                // 消息特性
                .setMessageFeature(MessageConstants.MessageFeature.DEFAULT.ordinal())
                // 消息类型
                .setMessageType(MessageConstants.ChatMessageType.FILE.ordinal())
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
     * 合并文件并且上传
     *
     * @param request
     *
     * @return
     */
    private ResponseResult mergeAndUploadFile(AttachmentMessage request) {

        // 素材准备
        Long senderId = Long.valueOf(request.getSenderId());
        if (Objects.isNull(request.getSenderId())) {
            senderId = UserContextHolder.get().getId();
        }
        Long receiverId = Long.valueOf(request.getReceiverId());
        String hash = request.getFileUploadId();

        // 合并文件
        MergeSplitFileRequest mergeRequest = new MergeSplitFileRequest()
                .setFileName(request.getOriginalFileName())
                .setSize(request.getSize())
                .setUploaderId(request.getSenderId());
        mergeRequest.setHash(hash).setMd5(hash).setUploadId(hash);
        ResponseResult mergeResult = this.splitUploadService.mergeSplitUploadFile(mergeRequest);
        if (BooleanUtils.isFalse(mergeResult.isSuccess())) {
            // 合并失败
            return ResponseResult.FAIL("合并文件失败，无法发送文件消息!");
        }
        File mergeFile = (File) mergeResult.getData();

        // 上传图片
        String fileUrl = this.minioService.putLocalFile(mergeFile.getAbsolutePath(), MinioConstant.BucketEnum.File.getBucket(), hash);
        if (StringUtils.isEmpty(fileUrl)) {
            // 上传失败
            return ResponseResult.FAIL("上传文件失败，无法发送文件消息!");
        }

        // 构建扩展信息
        FileMessageExtra extra = new FileMessageExtra();
        extra.setUploader(senderId)
                .setMd5(hash)
                .setUrl(fileUrl)
                .setOriginalFileName(mergeRequest.getFileName())
                .setFileName(mergeFile.getAbsolutePath())
                .setLocalPath(mergeFile.getAbsolutePath())
                .setSize(mergeFile.length())
                .setFileSubType(CloudResource.getResourceType(mergeRequest.getFileName()))
                .setExtension(FilenameUtils.getExtension(mergeRequest.getFileName()))
                .setReceiver(receiverId);

        return ResponseResult.SUCCESS(extra);
    }


}
