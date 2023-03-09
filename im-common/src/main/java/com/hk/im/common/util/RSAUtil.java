package com.hk.im.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : RSAUtil
 * @date : 2023/3/9 9:44
 * @description :RSA公钥/私钥/签名工具包
 * @Todo :
 * @Bug :
 * @Modified :字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 *  * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 *  * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * @Version : 1.0
 */
public class RSAUtil {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA 位数 如果采用2048 上面最大加密和最大解密则须填写:  245 256
     */
    private static final int INITIALIZE_LENGTH = 1024;


    /**
     * 获取密钥对
     * @return
     */
    public static Map<String, String> getKeyPair() {

        RSA rsa = new RSA();

        // 获取公钥,私钥
        String publicKey = rsa.getPublicKeyBase64();
        String privateKey = rsa.getPrivateKeyBase64();

        HashMap<String, String> map = new HashMap<>();
        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, privateKey);
        return map;
    }


    /**
     * 公钥加密
     * @param plain 明文
     * @param publicKeyString 公钥
     * @return
     */
    public static String encrypt(String plain, String publicKeyString) {
        RSA rsa = new RSA(null, publicKeyString);
        // 加密
        byte[] encrypt = rsa.encrypt(plain.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);

        // 转换为 Base64
        return Base64.encode(encrypt);
    }


    /**
     * 私钥解密
     * @param cipher 密文
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String cipher, String privateKey) {

        RSA rsa = new RSA(privateKey, null);
        byte[] decrypt = rsa.decrypt(Base64.decode(cipher), KeyType.PrivateKey);

        return new String(decrypt, StandardCharsets.UTF_8);
    }



}
