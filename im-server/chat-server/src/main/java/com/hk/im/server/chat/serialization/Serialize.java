package com.hk.im.server.chat.serialization;

import javax.sql.rowset.serial.SerialException;

/**
 * @author : HK意境
 * @ClassName : Serialize
 * @date : 2022/12/28 0:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface Serialize {


    /**
     * 序列化
     * @param object
     * @param <T>
     * @return
     * @throws SerialException
     */
    public <T> byte[] serialize(T object) throws SerialException;


    /**
     * 反序列化
     * @param clazz
     * @param data
     * @param <T>
     * @return
     * @throws SerialException
     */
    public <T> T deserialize(Class<T> clazz, byte[] data) throws SerialException;

}
