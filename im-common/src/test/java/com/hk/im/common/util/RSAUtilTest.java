package com.hk.im.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : HK意境
 * @ClassName : RSAUtilTest
 * @date : 2023/3/9 10:50
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class RSAUtilTest {

    private String password = "123456@hhHK";

    Map<String, String> keyPair = new HashMap<>();

    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSFhfwReB/0kbJC0apQvDQGHtgYuGAwBIJnatKahYF+vqMAdQPdNaiNjp3yroaey6dGfVYD0/S1EDl8mcAILEJYGYgQSQtJivEvuPQa6DeRuAXeJqzYq6AiGHArZAspbA0vxTk6aKDMaBRmQqV+kZChiakpwILBLqKP7SW3PcjOwIDAQAB";
    private String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJIWF/BF4H/SRskLRqlC8NAYe2Bi4YDAEgmdq0pqFgX6+owB1A901qI2OnfKuhp7Lp0Z9VgPT9LUQOXyZwAgsQlgZiBBJC0mK8S+49BroN5G4Bd4mrNiroCIYcCtkCylsDS/FOTpooMxoFGZCpX6RkKGJqSnAgsEuoo/tJbc9yM7AgMBAAECgYAv3IQwSLyCNELTuFmIt/FMxFCbphnKhGMEen81rKcVg4JVn326KKXvVEP4nlc1uNjvSKNsm+ujn725eJ8NWCiX8lgDjMGibr69KtQHyp/czjbqL09chyWKHp5BVQEBiu5fyS8FvmFyL9kB0nA1BXnbyQwVlskDobebPa5UvpedFQJBAM7Pcdo+avkOe3UkcC9v1u6DqGbS80JENqqbqZAQ+N+ZAbeFgXNKQrNnX2WtzCU/e5yFZIs1vbcP/4jLUmDg1nUCQQC01TMa8POlMVFOKyaFY7yzoJj8vfIpFQ4sSvJJ4ygEyBbOwBaGllbI//QDd+InBVfY2aGytyPYWWDFzs/PLbzvAkBEevKDRunbII2G45L2Uj1Ea3om2jFMvxOu+K50Evql559m6qFL0Mo3Z8JyA9O2fcfrs0vwwNHaPUTrQhA8HhdFAkAFM4ClzLplTPbWtb0E5yp9TCg6VMhLUDS5J2qyn1r+UDb/uVysgFR8sYf2NZOGpcuyDAhvmH7qcR+scNdfUSR3AkAXggLLLeRqsWSvNE5Ie7nAbj+sd3jpFnapqoPm4ZhGxs/Agldom1DktpqOjxVBbwNdUND3MldH/GLUPlk4LfaE";
    private String cipher = "TJ5BqsmG5UxYSoZHZ2iPsvjU91yzYLL+i4AlC20F1uTjQKmxafDVHUH4d/L4FpKsrnbnTynXAvGi8u6LNHZ1L0FVHCeqD2s803rowF+yB5sHUi6QVF0lre1SultHvqEpImYCM5DdGWxMF3ZsDY8mBDZYfgRX/QCTSMS8aMiEXL0=";

    @Test
    void getKeyPair() {
        keyPair = RSAUtil.getKeyPair();
        System.out.println(keyPair.get(RSAUtil.PUBLIC_KEY));
        System.out.println("---------------------------------------------------------------");
        System.out.println(keyPair.get(RSAUtil.PRIVATE_KEY));
    }

    @Test
    void encrypt() {

        String encrypt = RSAUtil.encrypt(password, publicKey);
        System.out.println(encrypt);
    }

    @Test
    void decrypt() {

        String decrypt = RSAUtil.decrypt(cipher, privateKey);
        System.out.println(decrypt);

    }


    @Test
    void all() {

        RSA rsa = new RSA();
        System.out.println(rsa.getPrivateKeyBase64());
        System.out.println(rsa.getPublicKeyBase64());

        byte[] encrypt = rsa.encrypt(password, KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);

        assert new String(decrypt, StandardCharsets.UTF_8).equals(password);
    }


    @Test
    void testMultipleCodec() {
        RSA rsa = new RSA();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setAccount("3161880795@qq.com");
        request.setCode("6666");
        request.setOldPassword("123456hh");
        request.setNewPassword("123456hh@HK.com");
        request.setType(2);

        // 多次加密多次解密
        byte[] encryptOldPassword = rsa.encrypt(request.getOldPassword().getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        byte[] encryptNewPassword = rsa.encrypt(request.getNewPassword().getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        request.setOldPassword(Base64.encode(encryptOldPassword));
        request.setNewPassword(Base64.encode(encryptNewPassword));
        byte[] encryptRequest = rsa.encrypt(toByteArray(request), KeyType.PublicKey);

        // 多次解密
        byte[] decryptRequest = rsa.decrypt(encryptRequest, KeyType.PrivateKey);
    }

    @Getter
    @Setter
    public static class ChangePasswordRequest {

        private String code;

        private String oldPassword;

        private String newPassword;

        private Integer type;

        private String account;

    }


    /**
     * 将Object对象转byte数组
     * @param obj byte数组的object对象
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

}

