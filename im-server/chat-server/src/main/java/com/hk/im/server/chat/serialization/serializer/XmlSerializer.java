package com.hk.im.server.chat.serialization.serializer;

import com.hk.im.server.chat.serialization.AbstractSerializer;

import javax.sql.rowset.serial.SerialException;

/**
 * @author : HK意境
 * @ClassName : XmlSerializer
 * @date : 2022/12/28 0:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class XmlSerializer extends AbstractSerializer {

    @Override
    public <T> byte[] serialize(T object) throws SerialException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] data) throws SerialException {
        return null;
    }
}
