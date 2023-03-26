package com.hk.im.admin.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.domain.bo.RoomNumber;
import com.hk.im.domain.entity.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author : HK意境
 * @ClassName : CacheConfig
 * @date : 2023/1/26 19:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Configuration
public class CacheConfig implements InitializingBean {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String, RoomNumber> redisTemplate;

    // 构造 Caffine 缓存 Bean 对象
    @Bean(name = "sequenceCache")
    public Cache<String, Sequence> getSequenceCache() {
        return Caffeine.newBuilder()
                // 过期时间: 一个小时
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();
    }


    /**
     * 缓存预热
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        // 是否存在 房间号集合
        Boolean exists = this.redisTemplate.hasKey(RedisConstants.ROOM_NUMBER_KEY);
        if (BooleanUtils.isFalse(exists)) {
            // 不存在, 放入缓存: 房间号：100 000 - 900 000
            RoomNumber[] roomNumbers = new RoomNumber[900000];
            for (int i = 0; i < 900000; i++) {
                int number = i + 100000;
                roomNumbers[i] =new RoomNumber().setNumber(number) ;
            }

            // 添加到Redis
            Long add = this.redisTemplate.opsForSet().add(RedisConstants.ROOM_NUMBER_KEY, roomNumbers);
            log.info("initial the room number set and count is {}.", add);
        }
    }
}
