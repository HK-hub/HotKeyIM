package com.hk.im.common.util;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author : HK意境
 * @ClassName : StreamUtil
 * @date : 2023/1/1 22:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class StreamUtil {

    /** * 流复制 */
    public static ByteArrayOutputStream copyInputStream(InputStream input) throws IOException {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // 定义一个缓存数组，临时存放读取的数组
            //经过测试，4*1024是一个非常不错的数字，过大过小都会比较影响性能
            byte[] buffer = new byte[4096];
            int length;
            while ((length= input.read(buffer)) > -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }


    public static ByteArrayInputStream convertToInputStream(OutputStream out) {
        ByteArrayOutputStream baos  = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }


    public static InputStream getImageStream(BufferedImage bufferedImage){
        InputStream is = null;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut;
        try {
            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufferedImage, "png",imOut);
            is= new ByteArrayInputStream(bs.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }


}
