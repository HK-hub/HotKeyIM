package com.hk.im.client.service;

import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : RSAService
 * @date : 2023/3/9 11:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface RSAService {

    /**
     * 生成密钥对
     * @return
     */
    public Map<String, String> generateKeyPairs();


    /**
     * 生成公钥
     * @return
     */
    public String generatePublicKey();


    /**
     * 更具公钥获取到对应的私钥
     * @param publicKey
     * @return
     */
    public String calculatePrivateKey(String publicKey);

    /**
     * 私钥解密
     * @param cipher
     * @param privateKey
     * @return
     */
    public String decrypt(String cipher, String privateKey);

}
