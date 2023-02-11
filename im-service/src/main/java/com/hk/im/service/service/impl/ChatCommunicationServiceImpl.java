package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.request.CreateCommunicationRequest;
import com.hk.im.infrastructure.mapper.ChatCommunicationMapper;
import com.hk.im.service.service.ChatCommunicationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : ChatCommunicationServiceImpl
 * @date : 2023/2/10 18:33
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class ChatCommunicationServiceImpl extends ServiceImpl<ChatCommunicationMapper, ChatCommunication>
        implements ChatCommunicationService {

    @Resource
    private ChatCommunicationMapper chatCommunicationMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 创建会话
     *
     * @param request: type 标识会话类型
     *
     * @return
     */
    @Override
    public ResponseResult createChatCommunication(CreateCommunicationRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getType()) || StringUtils.isEmpty(request.getReceiverId());
        if (BooleanUtils.isFalse(true)) {
            // 参数校验失败
            return ResponseResult.FAIL("参数校验失败!");
        }

        // 校验用户
        String userId = request.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return ResponseResult.FAIL("参数校验失败!").setResultCode(ResultCode.UNAUTHENTICATED);
        }

        // 数据准备
        Long receiverId = Long.valueOf(request.getReceiverId());
        Long senderId = Long.valueOf(userId);

        // 获取会话，判断会话是否存在
        ResponseResult talkResult = this.getChatCommunication(senderId,
                receiverId);
        if (BooleanUtils.isTrue(talkResult.isSuccess())) {
            // 会话已经存在
            return talkResult;
        }

        // 会话不存在，创建
        Integer type = request.getType();
        ChatCommunication communication = new ChatCommunication()
                //.setBelongUserId(senderId)
                .setSenderId(senderId)
                .setReceiverId(receiverId)
                .setSessionType(type);
        // 设置会话类型
        CommunicationConstants.SessionType sessionType = CommunicationConstants.SessionType.values()[type];
        if (sessionType == CommunicationConstants.SessionType.GROUP) {
            communication.setGroupId(receiverId);
        }

        // 保存
        boolean save = this.save(communication);
        if (BooleanUtils.isFalse(save)) {
            // 保存失败
            return ResponseResult.FAIL();
        }

        // 保存成功-缓存到redis
        String name = senderId < receiverId ? senderId + "-" + receiverId : receiverId + "-" + senderId;
        String key = RedisConstants.COMMUNICATION_KEY + RedisConstants.SEQUENCE_KEY + name;

        // 设置到 redis 缓存
        this.stringRedisTemplate.opsForValue().set(key, String.valueOf(0), 120, TimeUnit.MINUTES);

        return ResponseResult.SUCCESS(communication);
    }

    /**
     * 获取会话
     *
     * @param senderId
     * @param receiverId
     *
     * @return
     */
    @Override
    public ResponseResult getChatCommunication(Long senderId, Long receiverId) {

        ChatCommunication communication = this.chatCommunicationMapper.selectCommunication(senderId, receiverId);
        if (Objects.isNull(communication)) {
            // 会话不存在
            return ResponseResult.FAIL(communication);
        }

        return ResponseResult.SUCCESS(communication);
    }


    /**
     * 获取用户会话列表
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getUserCommunicationList(Long userId) {

        List<ChatCommunication> communicationList = this.lambdaQuery()
                .eq(ChatCommunication::getSenderId, userId)
                .or(wrapper -> {
                    wrapper.eq(ChatCommunication::getReceiverId, userId);
                })
                .orderByDesc(ChatCommunication::getUpdateTime)
                .list();
        if (CollectionUtils.isEmpty(communicationList)) {
            communicationList = Collections.emptyList();
        }
        return ResponseResult.SUCCESS(communicationList);
    }
}




