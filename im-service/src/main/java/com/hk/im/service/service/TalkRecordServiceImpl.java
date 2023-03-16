package com.hk.im.service.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.hk.im.client.service.AuthorizationService;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.client.service.TalkRecordService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.common.util.ObjectMapperUtil;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.BaseMessageExtra;
import com.hk.im.domain.dto.FileMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.DownloadMessageFileRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : TalkRecordServiceImpl
 * @date : 2023/3/15 22:15
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class TalkRecordServiceImpl implements TalkRecordService {

    @Resource
    private AuthorizationService authorizationService;
    @Resource
    private ChatMessageService chatMessageService;


    /**
     * 下载聊天记录中的文件
     * @param request
     * @return {@link ResponseResult} 返回文件下载链接，用于后续重定向
     */
    @Override
    public ResponseResult downloadRecordFile(DownloadMessageFileRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getAccessToken()) || StringUtils.isEmpty(request.getRecordId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL();
        }

        // 下载用户
        Long userId = UserContextHolder.get().getId();
        // 校验 accessToken
        String accessToken = request.getAccessToken();
        User user = this.authorizationService.authUserByToken(accessToken);

        if (Objects.isNull(user)) {
            // token 失效
            return ResponseResult.FAIL().setResultCode(ResultCode.UNAUTHORIZED);
        }

        // token 验证通过返回下载链接
        String recordId = request.getRecordId();
        ChatMessage fileMessage = this.chatMessageService.getById(recordId);

        if (Objects.isNull(fileMessage)) {
            // 文件消息不存在
            return ResponseResult.FAIL("消息记录文件不存在!");
        }

        return ResponseResult.SUCCESS(fileMessage.getUrl());
    }

    /**
     * 文件预览
     * @param request
     * @return
     */
    @Override
    public ResponseResult previewMessageFile(DownloadMessageFileRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getAccessToken()) || StringUtils.isEmpty(request.getRecordId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL();
        }

        // 下载用户
        Long userId = UserContextHolder.get().getId();
        // 校验 accessToken
        String accessToken = request.getAccessToken();
        User user = this.authorizationService.authUserByToken(accessToken);

        if (Objects.isNull(user)) {
            // token 失效
            return ResponseResult.FAIL().setResultCode(ResultCode.UNAUTHORIZED);
        }

        // token 验证通过返回下载链接
        String recordId = request.getRecordId();
        ChatMessage fileMessage = this.chatMessageService.getById(recordId);

        if (Objects.isNull(fileMessage)) {
            // 文件消息不存在
            return ResponseResult.FAIL("消息记录文件不存在!");
        }

        // 判断文件扩展类型是否支持预览
        FileMessageExtra extra = ObjectMapperUtil.OBJECT_MAPPER.convertValue(fileMessage.getExtra(), FileMessageExtra.class);
        String extension = extra.getExtension();
        // 文件是否支持预览
        if (!MessageConstants.enablePreviewFileType.contains(extension)) {
            // 该文件类型不自持预览
            return ResponseResult.FAIL().setDataAsMessage("抱歉该文件暂不支持预览功能!");
        }

        return ResponseResult.SUCCESS(fileMessage.getUrl());
    }
}
