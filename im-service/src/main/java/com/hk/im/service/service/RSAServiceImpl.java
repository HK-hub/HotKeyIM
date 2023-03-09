package com.hk.im.service.service;

import com.hk.im.client.service.RSAService;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : RSAServiceImpl
 * @date : 2023/3/9 11:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class RSAServiceImpl implements RSAService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 生成密钥对，保存到redis
     * @return
     */
    @Override
    public Map<String, String> generateKeyPairs() {

        // 生成密钥对
        Map<String, String> keyPair = RSAUtil.getKeyPair();
        String privateKey = keyPair.get(RSAUtil.PRIVATE_KEY);
        String publicKey = keyPair.get(RSAUtil.PUBLIC_KEY);

        // 保存到redis: <公钥,私钥>
        String redisKey = RedisConstants.RSA_KEY + publicKey;
        this.stringRedisTemplate.opsForValue().set(redisKey, privateKey, 60, TimeUnit.SECONDS);

        return keyPair;
    }


    /**
     * 生成公钥
     * @return
     */
    @Override
    public String generatePublicKey() {
        // 生成密钥对
        Map<String, String> keyPairs = this.generateKeyPairs();

        return keyPairs.get(RSAUtil.PUBLIC_KEY);
    }


    /**
     * 根据公钥计算出私钥
     * @param publicKey
     * @return
     */
    @Override
    public String calculatePrivateKey(String publicKey) {

        String key = RedisConstants.RSA_KEY + publicKey;
        String privateKey = this.stringRedisTemplate.opsForValue().get(publicKey);
        return privateKey;
    }


    /**
     * 私钥解密
     * @param cipher 密文
     * @param privateKey 密钥
     * @return
     */
    @Override
    public String decrypt(String cipher, String privateKey) {
        return RSAUtil.decrypt(cipher, privateKey);
    }
}
