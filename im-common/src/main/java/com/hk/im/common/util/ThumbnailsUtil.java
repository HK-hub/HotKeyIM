package com.hk.im.common.util;


import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author : HK意境
 * @ClassName : ThumbnailsUtil
 * @date : 2023/1/1 21:56
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ThumbnailsUtil {

    /**
     * 指定大小缩放 若图片横比width小，高比height小，放大
     * 若图片横比width小，高比height大，高缩小到height，图片比例不变
     * 若图片横比width大，高比height小，横缩小到width，图片比例不变
     * 若图片横比width大，高比height大，图片按比例缩小，横为width或高为height
     *
     * @param resource  源文件路径
     * @param width     宽
     * @param height    高
     * @param tofile    生成文件路径
     */
    public static void changeSize(String resource, int width, int height, String tofile) {
        try {
            Thumbnails.of(resource).size(width, height)
                    .toFile(tofile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 指定大小缩放
     * @param inputStream 输入流
     * @param outputStream 输出流
     * @param width
     * @param height
     */
    public static void changeSizeByStream(InputStream inputStream, OutputStream outputStream, int width, int height) {

        try{
            Thumbnails.of(inputStream)
                    .size(width, height)
                    .toOutputStream(outputStream);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 指定大小缩放，输出为BufferedImage
     * @param inputStream
     * @param width
     * @param height
     * @param tofile
     * @return
     */
    public static BufferedImage changeSizeToBufferedImage(InputStream inputStream, int width, int height) {
        try {
            BufferedImage bufferedImage = Thumbnails.of(inputStream).size(width, height)
                    .asBufferedImage();
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 指定比例缩放 scale(),参数小于1,缩小;大于1,放大
     *
     * @param resource   源文件路径
     * @param scale      指定比例
     * @param tofile     生成文件路径
     */
    public static void changeScale(String resource, double scale, String tofile) {
        try {
            Thumbnails.of(resource).scale(scale).toFile(tofile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加水印 watermark(位置,水印,透明度)
     *
     * @param resource  源文件路径
     * @param p         水印位置
     * @param watermark   水印文件路径
     * @param opacity   水印透明度
     * @param tofile    生成文件路径
     */
    public static void watermark(String resource, Positions p, String watermark, float opacity, String tofile) {
        try {
            Thumbnails.of(resource).scale(1).watermark(p, ImageIO.read(new File(watermark)), opacity).toFile(tofile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 图片旋转 rotate(度数),顺时针旋转
     *
     * @param resource  源文件路径
     * @param rotate    旋转度数
     * @param tofile    生成文件路径
     */
    public static void rotate(String resource, double rotate, String tofile) {
        try {
            Thumbnails.of(resource).scale(1).rotate(rotate).toFile(tofile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 图片裁剪 sourceRegion()有多种构造方法，示例使用的是sourceRegion(裁剪位置,宽,高)
     *
     * @param resource  源文件路径
     * @param p         裁剪位置
     * @param width     裁剪区域宽
     * @param height    裁剪区域高
     * @param tofile    生成文件路径
     */
    public static void region(String resource, Positions p, int width, int height, String tofile) {
        try {
            Thumbnails.of(resource).scale(1).sourceRegion(p, width, height).toFile(tofile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
