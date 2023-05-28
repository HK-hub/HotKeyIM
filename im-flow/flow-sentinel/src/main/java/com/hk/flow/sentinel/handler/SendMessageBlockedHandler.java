package com.hk.flow.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.message.chat.AttachmentMessage;
import com.hk.im.domain.message.chat.ImageMessage;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.request.CodeMessageRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : SendMessageBlockedHandler
 * @date : 2023/5/26 10:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class SendMessageBlockedHandler {

    /**
     * 处理发送文本消息熔断异常
     * @param message
     * @param e
     * @return
     */
    public static ResponseResult handleTextBlock(TextMessage message, BlockException e) {

        // 触发限流规则
        log.info("触发限流规则：{}, parameter={}, e={}", "/send/text/message", message, e);
        return ResponseResult.FAIL().setMessage("发送消息频率过快!，请休息一下哦");
    }


    /**
     * 处理发送图片消息熔断异常
     * @param message
     * @param e
     * @return
     */
    public static ResponseResult handleImageBlock(ImageMessage message, BlockException e) {

        // 触发限流规则
        log.info("触发限流规则：{}, parameter={}, e={}", "/send/image/message", message, e);
        return ResponseResult.FAIL().setMessage("发送消息频率过快!，请休息一下哦");
    }

    /**
     * 处理发送文件消息熔断异常
     * @param message
     * @param e
     * @return
     */
    public static ResponseResult handleFileBlock(AttachmentMessage message, BlockException e) {

        // 触发限流规则
        log.info("触发限流规则：{}, parameter={}, e={}", "/send/file/message", message, e);
        return ResponseResult.FAIL().setMessage("发送消息频率过快!，请休息一下哦");
    }

    /**
     * 处理发送代码消息熔断异常
     * @param message
     * @param e
     * @return
     */
    public static ResponseResult handleCodeBlock(CodeMessageRequest message, BlockException e) {

        // 触发限流规则
        log.info("触发限流规则：{}, parameter={}, e={}", "/send/code/message", message, e);
        return ResponseResult.FAIL().setMessage("发送消息频率过快!，请休息一下哦");
    }


}
