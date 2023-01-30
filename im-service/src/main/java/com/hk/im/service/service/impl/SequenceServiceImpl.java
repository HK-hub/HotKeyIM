package com.hk.im.service.service.impl;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.GroupSetting;
import com.hk.im.domain.entity.Sequence;
import com.hk.im.infrastructure.event.communication.event.RefreshSequenceEvent;
import com.hk.im.infrastructure.mapper.SequenceMapper;
import com.hk.im.service.service.FriendService;
import com.hk.im.service.service.GroupSettingService;
import com.hk.im.service.service.SequenceService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.ApplicationContext;
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

    @Resource(name = "sequenceCache")
    private Cache<String, Sequence> sequenceCache;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private FriendService friendService;
    @Resource
    private GroupSettingService groupSettingService;
    @Resource
    private ApplicationContext applicationContext;


    /**
     * 获取一个发号器
     * @param communicationId
     * @param participantId
     * @return
     */
    @Override
    public ResponseResult getSequence(Long communicationId, Long participantId) {

        // 参数校验
        if (Objects.isNull(communicationId) || Objects.isNull(participantId)) {
            return ResponseResult.FAIL("会话参数不完整!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 数据库是否存在
        Sequence sequence = this.lambdaQuery()
                .eq(Sequence::getCommunicationId, communicationId)
                .eq(Sequence::getParticipantId, participantId)
                .one();
        if (Objects.isNull(sequence)) {
            // 会话发号器不存在，查看好友关系，群聊关系决定是否创建发号器
            Friend relationship = this.friendService.isFriendRelationship(communicationId, participantId);
            if (Objects.isNull(relationship)) {
                // 非好友关系 -> 可能群聊发起的临时会话
                return ResponseResult.FAIL("抱歉，你们不是好友!");
            }
            ResponseResult result = this.createSequence(communicationId, participantId);
            if (result.isSuccess()) {
                sequence = (Sequence) result.getData();
            }
        }

        return ResponseResult.SUCCESS(sequence);
    }

    /**
     * 创建一个会话发号器
     * @param communicationId
     * @param participantId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createSequence(Long communicationId, Long participantId) {

        // 参数校验
        if (Objects.isNull(communicationId) || Objects.isNull(participantId)) {
            return ResponseResult.FAIL("会话参数不完整!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 会话不存在，创建会话
        String name = communicationId + "-" + participantId;
        Sequence sequence = new Sequence()
                .setName(name)
                .setCommunicationId(communicationId)
                .setParticipantId(participantId)
                .setMax(1001L)
                .setCurrent(0L);
        boolean save = this.save(sequence);
        if (BooleanUtils.isFalse(save)) {
            // 创建失败
            return ResponseResult.FAIL("创建会话失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // 将会话保存到 缓存 中
        String key = RedisConstants.COMMUNICATION_KEY + RedisConstants.SEQUENCE_KEY + communicationId + "-" + participantId;
        sequenceCache.put(key, sequence);
        stringRedisTemplate.opsForValue()
                .set(key, String.valueOf(sequence.getCurrent()), 60, TimeUnit.MINUTES);

        return ResponseResult.SUCCESS(sequence);
    }


    /**
     * 获取下一个会话消息ID
     *
     * @param communicationId 会话主要人id: 可以为群id
     * @param participantId   会话参与人id
     *
     * @return
     */
    @Override
    public synchronized ResponseResult nextId(Long communicationId, Long participantId) {

        // Redis 中只存储发号器的计数， Caffeine 中存储发号器
        String key = RedisConstants.COMMUNICATION_KEY + RedisConstants.SEQUENCE_KEY + communicationId + "-" + participantId;

        // 获取缓存中的 sequence 发号器
        Sequence sequence = sequenceCache.getIfPresent(key);
        if (Objects.isNull(sequence)) {
            // 已经过期或不存在, 从数据库中获取
            ResponseResult result = this.getSequence(communicationId, participantId);
            if (BooleanUtils.isFalse(result.isSuccess())) {
                return result.setResultCode(ResultCode.SERVER_BUSY);
            }
            sequence = (Sequence) result.getData();
            // 更新缓存
            sequenceCache.put(key, sequence);
        }

        // 按照步长自增
        Long increment = stringRedisTemplate.opsForValue().increment(key, sequence.getStep());
        if (Objects.isNull(increment)) {
            // redis 还不存在该会话
            // 设置 sequence
            increment = sequence.getMax();
            stringRedisTemplate.opsForValue()
                    .set(key, String.valueOf(increment), 60, TimeUnit.MINUTES);
        }

        // 更新cache, 重置过期时间
        sequenceCache.put(key, sequence.setCurrent(increment));
        // TODO 发送事件，异步刷新发号器: buffer 计算
        applicationContext.publishEvent(new RefreshSequenceEvent(this, sequence));

        return ResponseResult.SUCCESS(increment);
    }
}




