package com.hk.im.service.worker;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.ConversationMessageExtra;
import com.hk.im.domain.dto.FileMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.request.InviteVideoCallInviteRequest;
import com.hk.im.domain.request.InviteVideoCallRequest;
import com.hk.im.infrastructure.event.file.event.UploadFileEvent;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import com.hk.im.infrastructure.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : VideoMessageWorker
 * @date : 2023/3/22 15:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class VideoMessageWorker {

    @Resource
    private ApplicationContext applicationContext;


    /**
     * 处理邀请视频通话
     * @param request
     * @return
     */
    public ResponseResult inviteVideoCall(InviteVideoCallInviteRequest request) {

        // 参数校验
        log.info("InviteVideoCall: {}", request);
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getCmd()) || StringUtils.isEmpty(request.getListener());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 发送视频通话邀请事件
        this.applicationContext.publishEvent();

        // 发送消息成功
        return ResponseResult.SUCCESS(null).setMessage("消息发送成功!");
    }


    // 加入视频通话
    public ResponseResult joinVideoCall(InviteVideoCallRequest request) {



        return null;
    }

    // 拒绝视频通话
    public ResponseResult rejectVideoCall(InviteVideoCallRequest request) {

        return null;
    }



}
