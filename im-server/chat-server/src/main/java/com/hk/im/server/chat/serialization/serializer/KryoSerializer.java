package com.hk.im.server.chat.serialization.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hk.im.server.chat.serialization.AbstractSerializer;

import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author : HK意境
 * @ClassName : KryoSerializer
 * @date : 2022/12/29 14:23
 * @description : Kryo 序列化协议 序列化器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class KryoSerializer extends AbstractSerializer {

    private Class<?> clazz;

    /**
     * 由于 Kryo 不是线程安全的。每个线程都应该有自己的 Kryo，Input 和 Output 实例。
     * 所以，使用 ThreadLocal 存放 Kryo 对象
     */
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(clazz);
        // 默认值为true,是否关闭注册行为,关闭之后可能存在序列化问题，一般推荐设置为 true
        kryo.setReferences(true);
        // 默认值为false,是否关闭循环引用，可以提高性能，但是一般不推荐设置为 true
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    public KryoSerializer(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public <T> byte[] serialize(T object) throws SerialException {

        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Output output = new Output(bos)
        ) {
            Kryo kryo = kryoThreadLocal.get();
            // Object->byte:将对象序列化为byte数组
            kryo.writeObject(output,object);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerialException("kryo serialize the message object failed: " + e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] data) throws SerialException {

        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                Input input = new Input(bis);
        ) {
            Kryo kryo = kryoThreadLocal.get();
            // byte-> Object:从byte数组中反序列化出对对象
            T object = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerialException("kryo deserialize the message object failed: " + e.getMessage());
        }
    }
}
