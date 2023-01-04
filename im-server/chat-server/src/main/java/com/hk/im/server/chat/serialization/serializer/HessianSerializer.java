package com.hk.im.server.chat.serialization.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.hk.im.server.chat.serialization.AbstractSerializer;

import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author : HK意境
 * @ClassName : HessianSerializer
 * @date : 2022/12/28 1:01
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class HessianSerializer extends AbstractSerializer {

    @Override
    public <T> byte[] serialize(T object) throws SerialException {

        HessianOutput hessianOutput = null;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ) {
            hessianOutput = new HessianOutput(bos);
            hessianOutput.writeObject(object);
            hessianOutput.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerialException("hessian serialize the message object failed: " + e.getMessage());
        } finally {
            assert hessianOutput != null;
            try {
                hessianOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] data) throws SerialException {

        HessianInput hessianInput = new HessianInput();
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(data)
        ) {
            hessianInput.init(bis);
            T object = (T) hessianInput.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            hessianInput.close();
        }
        return null;
    }
}
