package com.hk.im.server.chat.serialization.serializer;


import com.hk.im.server.chat.serialization.AbstractSerializer;

import javax.sql.rowset.serial.SerialException;
import java.io.*;

/**
 * @author : HK意境
 * @ClassName : JdkSerializer
 * @date : 2022/12/28 0:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class JdkSerializer extends AbstractSerializer {

    @Override
    public <T> byte[] serialize(T object) throws SerialException {
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            // 写入对象
            oos.writeObject(object);
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerialException("java serialize the message object failed: " + e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] data) throws SerialException {
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis)) {

            // 写入对象
            T object = (T) ois.readObject();
            return object;
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerialException("java deserialize the message object failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SerialException("java deserialize the bytes data to the message object failed: " + e.getMessage());
        }
    }
}
