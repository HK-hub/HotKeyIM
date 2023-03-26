package com.hk.im.service.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.FriendService;
import com.hk.im.client.service.MessageFlowService;
import com.hk.im.client.service.SequenceService;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.RoomNumber;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.entity.*;
import com.hk.im.infrastructure.event.communication.event.RefreshSequenceEvent;
import com.hk.im.infrastructure.mapper.SequenceMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : SequenceServiceImpl
 * @date : 2023/1/26 18:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class SequenceServiceImpl extends ServiceImpl<SequenceMapper, Sequence> implements SequenceService {

    @Resource
    private MessageFlowService messageFlowService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private RedisTemplate<String, RoomNumber> redisTemplate;
    @Resource
    private FriendService friendService;
    @Resource
    private SequenceMapper sequenceMapper;
    @Resource
    private ApplicationContext applicationContext;



    /**
     * 获取一个发号器
     *
     * @param senderId
     * @param receiverId
     *
     * @return
     */
    @Override
    @Deprecated
    public ResponseResult getSequence(Long senderId, Long receiverId) {

        // 参数校验
        if (Objects.isNull(senderId) || Objects.isNull(receiverId)) {
            return ResponseResult.FAIL("会话参数不完整!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 数据库是否存在
        Sequence sequence = this.sequenceMapper.selectSessionSequence(senderId, receiverId);

        if (Objects.isNull(sequence)) {
            // 会话发号器不存在，查看好友关系，群聊关系决定是否创建发号器
            Friend relationship = this.friendService.isFriendRelationship(senderId, receiverId);
            if (Objects.isNull(relationship)) {
                // 非好友关系 -> 可能群聊发起的临时会话
                return ResponseResult.FAIL("抱歉，你们不是好友!");
            }
            ResponseResult result = this.createSequence(senderId, receiverId);
            if (result.isSuccess()) {
                sequence = (Sequence) result.getData();
            }
        }

        return ResponseResult.SUCCESS(sequence);
    }

    /**
     * 创建一个会话发号器
     *
     * @param senderId
     * @param receiverId
     *
     * @return
     */
    @Override
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createSequence(Long senderId, Long receiverId) {

      return null;
    }


    /**
     * 获取下一个消息消息ID
     *
     * @param senderId 会话主要人id: 可以为群id
     * @param receiverId   会话参与人id
     *
     * @return
     */
    @Override
    public synchronized ResponseResult nextId(Long senderId, Long receiverId, Integer talkType) {

        // Redis 中只存储发号器的计数， TODO Caffeine 中存储发号器
        String name = senderId < receiverId ? senderId + "-" + receiverId : receiverId + "-" + senderId;
        // 根据 talkType 计算 name
        if (CommunicationConstants.SessionType.GROUP.ordinal() == talkType) {
            // 群聊
            name = String.valueOf(receiverId);
        }
        String key = RedisConstants.COMMUNICATION_KEY + RedisConstants.SEQUENCE_KEY + name;

        // 缓存中是否存在会话
        Boolean exists = stringRedisTemplate.hasKey(key);
        if (BooleanUtils.isFalse(exists)) {
            // 会话不存在,查询最大消息Id ,进行设置
            MessageFlow messageFlow = this.messageFlowService.getCommunicationMaxMessageSequence(senderId, receiverId);
            if (Objects.isNull(messageFlow)) {
                messageFlow = new MessageFlow().setSequence(0L);
            }
            // 设置自增缓存: 8 小时
            this.stringRedisTemplate.opsForValue()
                    .set(key, String.valueOf(messageFlow.getSequence()), RedisConstants.SEQUENCE_TTL, TimeUnit.SECONDS);
        }

        // 按照步长自增
        Long nextId = stringRedisTemplate.opsForValue().increment(key);
        if (Objects.isNull(nextId)) {
            // 自增失败
            return ResponseResult.FAIL().setResultCode(ResultCode.SERVER_ERROR);
        }

        // 更新cache, 重置过期时间
        // TODO 发送事件，异步刷新发号器: buffer 计算
        this.applicationContext.publishEvent(new RefreshSequenceEvent(this,
                new Sequence().setSenderId(senderId).setReceiverId(receiverId)));

        return ResponseResult.SUCCESS(nextId);
    }


    /**
     * 获取一个房间号
     * @param hostId
     * @return
     */
    @Override
    public ResponseResult getRoomId(Long hostId) {

        // 采用Redis Set集合
        Boolean flag = true;
        RoomNumber roomNumber = null;
        for (int i = 0; i < 900000 && flag; i++) {
            roomNumber = this.redisTemplate.opsForSet().pop(RedisConstants.ROOM_NUMBER_KEY);
            if (Objects.isNull(roomNumber)) {
                roomNumber = new RoomNumber().setInUse(Boolean.TRUE);
            }
            Boolean inUse = roomNumber.getInUse();
            if (BooleanUtils.isFalse(inUse)) {
                // 没有使用 -> 使用
                roomNumber.setInUse(Boolean.TRUE)
                        .setHostId(hostId);
                flag = false;
            }
            this.redisTemplate.opsForSet().add(RedisConstants.ROOM_NUMBER_KEY, roomNumber);
        }

        if (BooleanUtils.isTrue(flag)) {
            // 未找到房间号
            return ResponseResult.FAIL();
        }

        return ResponseResult.SUCCESS(roomNumber);
    }
}




