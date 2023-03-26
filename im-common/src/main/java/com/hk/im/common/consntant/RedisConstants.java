package com.hk.im.common.consntant;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : RedisConstants
 * @date : 2022/10/29 10:01
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class RedisConstants {

    // redis db 数量
    public static final int DATABASE_NUMBER = 10;
    // redis 空数据
    public static final String EMPTY_DATA_STRING = "empty:data";
    // redis 热点数据缓存时间: 24 小时
    public static final Long HOT_DATA_TTL = 24L;
    // 随机时间: 60 seconds
    public static final Long RANDOM_KEY_TTL = 60L ;
    // 缓存重建自旋次数
    public static final Integer CACHE_REBUILD_COUNT = 10;
    // 缓存相关线程池名称
    public static final String CACHE_THREAD_POOL = "thread:pool:cache:";
    // 分布式锁前缀
    public static final String LOCK_PREFIX = "lock:";


    // 验证码缓存 key
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final String MODIFY_PASSWORD_KEY = "modify:password:";
    public static final Long LOGIN_CODE_TTL = 2L;

    // 用户token 缓存key
    public static final String LOGIN_USER_KEY = "login:accessToken:";
    // 用户
    public static final String LOGIN_USER_MAP = "login:user:map";
    public static final Long LOGIN_USER_TTL = 36000L;
    // 用户 token 过期时间：3600 秒=1小时
    public static final Long ACCESS_TOKEN_TTL = 3600L * 24;

    // 空数据 缓存时间
    public static final Long CACHE_NULL_TTL = 2L;

    // 商铺 缓存时间
    public static final Long CACHE_SHOP_TTL = 30L;
    // 店铺缓存 key
    public static final String CACHE_SHOP_KEY = "cache:shop:";
    // 店铺类型缓存 key
    public static final String CACHE_SHOP_TYPE_KEY = "cache:shop:type:";
    // 群聊缓存key
    public static final String CACHE_GROUP_KEY = "cache:group:";
    // 群聊缓存过期时间
    public static final Long CACHE_GROUP_TTL = 10L;
    // 店铺下单锁key
    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;


    // 秒杀库存key
    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    // 探店博文点赞key
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    // 博主关注列表 key
    public static final String UP_FOLLOW_KEY = "follows:";
    public static final String FEED_KEY = "feed:";
    // 附近商铺 key
    public static final String SHOP_GEO_KEY = "shop:geo:";
    // 附件商铺距离
    public static final Integer SHOP_GEO_DISTANCE = 5000;
    public static final String USER_SIGN_KEY = "sign:";

    // 会话
    public static final String COMMUNICATION_KEY = "communication:";
    // sequence 发号器
    public static final String SEQUENCE_KEY = "sequence:";
    // 会话消息id 发号器 ttl : 8 小时 = 3600 * 8
    public static final long SEQUENCE_TTL = 3600 * 8;

    // RSA 密钥
    public static final String RSA_KEY = "security:rsa:key:";
    public static final String UPLOAD_TOKEN = "upload:token:";


    // 房间号集合
    public static final String ROOM_NUMBER_KEY = "rtc:room:number:";


    /**
     * 判断 key 值是否存在于 redis 中
     * 使用 bloom filter
     * @param key
     * @return
     */
    public static Boolean isExistsKey(String key){
        return Boolean.TRUE;
    }


    /**
     * 判断缓存中某个 value 是否是 空数据
     * @param value
     * @return
     */
    public static Boolean isEmptyData(String value){
        // null or len = 0
        if (StringUtils.isEmpty(value)) {
            return Boolean.TRUE;
        }

        // 空白数据: jdk 11
        if (value.isBlank()) {
            return Boolean.TRUE;
        }

        // 空数据
        if (Objects.equals(value, RedisConstants.EMPTY_DATA_STRING)) {
            // empty data
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * key 值过期时间再基础值之上加上随机过期时间避免缓存雪崩
     * 缓存击穿：某个时间点大量缓存key 过期
     * @param standard
     * @param random
     * @return
     */
    public static Long getRandomKeyTtl(Long standard, Long random){
        // 底层使用 线程安全的 threadLocalRandom 类
        return RandomUtils.nextLong(standard, standard + random);
    }

    public static Long getRandomKeyTtl(Long standard){
        // 底层使用 线程安全的 threadLocalRandom 类
        long ttl = RandomUtils.nextLong(standard, standard + RedisConstants.RANDOM_KEY_TTL);
        return ttl;
    }

}
