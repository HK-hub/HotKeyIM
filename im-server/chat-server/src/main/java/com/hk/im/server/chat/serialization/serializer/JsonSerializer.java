package com.hk.im.server.chat.serialization.serializer;

import com.google.gson.Gson;
import com.hk.im.server.chat.serialization.AbstractSerializer;

import javax.sql.rowset.serial.SerialException;
import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : JsonSerializer
 * @date : 2022/12/28 0:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class JsonSerializer extends AbstractSerializer {

    @Override
    public <T> byte[] serialize(T object) throws SerialException {
        String json = new Gson().toJson(object);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] data) throws SerialException {
        String json = new String(data, StandardCharsets.UTF_8);
        T object = new Gson().fromJson(json, clazz);
        return object;
    }
}
