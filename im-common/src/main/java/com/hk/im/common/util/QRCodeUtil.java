package com.hk.im.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Slf4j
public class QRCodeUtil {
    /**
     * CODE_WIDTH：二维码宽度，单位像素
     * CODE_HEIGHT：二维码高度，单位像素
     * FRONT_COLOR：二维码前景色，0x000000 表示黑色
     * BACKGROUND_COLOR：二维码背景色，0xFFFFFF 表示白色
     * 演示用 16 进制表示，和前端页面 CSS 的取色是一样的，注意前后景颜色应该对比明显，如常见的黑白
     */
    private static final int CODE_WIDTH = 400;
    private static final int CODE_HEIGHT = 400;
    private static final int FRONT_COLOR = 0x000000;
    private static final int BACKGROUND_COLOR = 0xFFFFFF;
    /**
     * LOGO 默认宽度
     */
    private static final Integer LOGO_WIDTH = 22;
    /**
     * LOGO 默认高度
     */
    private static final Integer LOGO_HEIGHT = 22;
    /**
     * 图片格式
     */
    private static final String IMAGE_FORMAT = "png";
    private static final String CHARSET = "utf-8";

    /**
     * @param codeContent        二维码参数内容，如果是一个网页地址，如 https://www.baidu.com/ 则 微信扫一扫会直接进入此地址， 如果是一些参数，如
     *                           1541656080837，则微信扫一扫会直接回显这些参数值
     * @param codeImgFileSaveDir 二维码图片保存的目录,如 D:/codes
     * @param fileName           二维码图片文件名称，带格式,如 123.png
     */
    public static void createCodeToFile(String codeContent, File codeImgFileSaveDir, String fileName) {
        try {
            if (codeContent == null || "".equals(codeContent)) {
                log.info("二维码内容为空，不进行操作...");
                return;
            }
            codeContent = codeContent.trim();
            if (codeImgFileSaveDir == null || codeImgFileSaveDir.isFile()) {
                codeImgFileSaveDir = FileSystemView.getFileSystemView().getHomeDirectory();
                log.info("二维码图片存在目录为空，默认放在桌面...");
            }
            if (!codeImgFileSaveDir.exists()) {
                codeImgFileSaveDir.mkdirs();
                log.info("二维码图片存在目录不存在，开始创建...");
            }
            if (fileName == null || "".equals(fileName)) {
                fileName = System.currentTimeMillis() + ".png";
                log.info("二维码图片文件名为空，随机生成 png 格式图片...");
            }

            BufferedImage bufferedImage = getBufferedImage(codeContent);

            /*
             * javax.imageio.ImageIO：java扩展的图像IO
             * write(RenderedImage im, String formatName, File output)
             *       im：待写入的图像， formatName：图像写入的格式，output：写入的图像文件，文件不存在时会自动创建
             */
            File codeImgFile = new File(codeImgFileSaveDir, fileName);
            ImageIO.write(bufferedImage, "png", codeImgFile);

            log.info("二维码图片生成成功：" + codeImgFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成二维码并输出到输出流, 通常用于输出到网页上进行显示
     * 输出到网页与输出到磁盘上的文件中，区别在于最后一句 ImageIO.write
     * write(RenderedImage im,String formatName,File output)：写到文件中
     * write(RenderedImage im,String formatName,OutputStream output)：输出到输出流中
     *
     * @param codeContent  ：二维码内容
     * @param outputStream ：输出流，比如 HttpServletResponse 的 getOutputStream
     */
    public static void createCodeToOutputStream(String codeContent, OutputStream outputStream) {
        try {
            if (codeContent == null || "".equals(codeContent.trim())) {
                log.info("二维码内容为空，不进行操作...");
                return;
            }
            codeContent = codeContent.trim();

            BufferedImage bufferedImage = getBufferedImage(codeContent);
            /*
             * 区别就是以一句，输出到输出流中，如果第三个参数是 File，则输出到文件中
             */
            ImageIO.write(bufferedImage, "png", outputStream);
            log.info("二维码图片生成到输出流成功...");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发生错误: {}!", e.getMessage());
        }
    }


    public static BufferedImage getBufferedImage(String codeContent) {
        /*
         * com.google.zxing.EncodeHintType：编码提示类型,枚举类型
         * EncodeHintType.CHARACTER_SET：设置字符编码类型
         * EncodeHintType.ERROR_CORRECTION：设置误差校正
         * ErrorCorrectionLevel：误差校正等级，L = ~7% correction、M = ~15% correction、Q = ~25% correction、H = ~30% correction
         *   不设置时，默认为 L 等级，等级不一样，生成的图案不同，但扫描的结果是一样的
         * EncodeHintType.MARGIN：设置二维码边距，单位像素，值越小，二维码距离四周越近
         */
        Map<EncodeHintType, Object> hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);

        /*
         * MultiFormatWriter:多格式写入，这是一个工厂类，里面重载了两个 encode 方法，用于写入条形码或二维码
         *      encode(String contents,BarcodeFormat format,int width, int height,Map<EncodeHintType,?> hints)
         *      contents:条形码/二维码内容
         *      format：编码类型，如 条形码，二维码 等
         *      width：码的宽度
         *      height：码的高度
         *      hints：码内容的编码类型
         * BarcodeFormat：枚举该程序包已知的条形码格式，即创建何种码，如 1 维的条形码，2 维的二维码 等
         * BitMatrix：位(比特)矩阵或叫2D矩阵，也就是需要的二维码
         */
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(codeContent, BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT,
                    hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        /*
         * java.awt.image.BufferedImage：具有图像数据的可访问缓冲图像，实现了 RenderedImage 接口
         * BitMatrix 的 get(int x, int y) 获取比特矩阵内容，指定位置有值，则返回true，将其设置为前景色，否则设置为背景色
         * BufferedImage 的 setRGB(int x, int y, int rgb) 方法设置图像像素
         *      x：像素位置的横坐标，即列
         *      y：像素位置的纵坐标，即行
         *      rgb：像素的值，采用 16 进制,如 0xFFFFFF 白色
         */
        BufferedImage bufferedImage = new BufferedImage(CODE_WIDTH, CODE_HEIGHT, BufferedImage.TYPE_INT_BGR);
        for (int x = 0; x < CODE_WIDTH; x++) {
            for (int y = 0; y < CODE_HEIGHT; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? FRONT_COLOR : BACKGROUND_COLOR);
            }
        }
        return bufferedImage;
    }


    /**
     * 根据本地二维码图片解析二维码内容 注：图片必须是二维码图片，但也可以是微信用户二维码名片，上面有名称、头像也是可以的）
     *
     * @param file 本地二维码图片文件,如 E:\logs\2.jpg
     *
     * @return
     *
     * @throws Exception
     */
    public static String parseQRCodeByFile(File file) {
        String resultStr = null;
        if (file == null || file.isDirectory() || !file.exists()) {
            return resultStr;
        }
        try {
            /*
             * ImageIO的BufferedImage read(URL input)方法用于读取网络图片文件转为内存缓冲图像
             * 同理还有：read(File input)、read(InputStream input)、、read(ImageInputStream stream)
             */
            BufferedImage bufferedImage = ImageIO.read(file);
            /*
             * com.google.zxing.client.j2se.BufferedImageLuminanceSource：缓冲图像亮度源
             * 将 java.awt.image.BufferedImage 转为 zxing 的 缓冲图像亮度源
             * 关键就是下面这几句：HybridBinarizer 用于读取二维码图像数据，BinaryBitmap 二进制位图
             */
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable hints = new Hashtable();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            /*
             * 如果图片不是二维码图片，则 decode 抛异常：com.google.zxing.NotFoundException
             * MultiFormatWriter 的 encode 用于对内容进行编码成 2D 矩阵
             * MultiFormatReader 的 decode 用于读取二进制位图数据
             */
            Result result = new MultiFormatReader().decode(bitmap, hints);
            resultStr = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
            log.error("图片非二维码图片, 路径是: {}!", file.getPath());
        }
        return resultStr;
    }


    /**
     * 根据网络二维码图片解析二维码内容, 区别仅仅在于 ImageIO.read(url); 这一个重载的方法）
     *
     * @param url 二维码图片网络地址，如 https://res.wx.qq.com/mpres/htmledition/images/mp_qrcode3a7b38.gif
     *
     * @return
     *
     * @throws Exception
     */
    public static String parseQRCodeByUrl(URL url) {
        String resultStr = null;
        if (url == null) {
            return resultStr;
        }
        try {
            /*
             * ImageIO 的 BufferedImage read(URL input) 方法用于读取网络图片文件转为内存缓冲图像
             * 同理还有：read(File input)、read(InputStream input)、、read(ImageInputStream stream)
             * 如果图片网络地址错误，比如不能访问，则 read 抛异常：javax.imageio.IIOException: Can't get input stream from URL!
             */
            BufferedImage bufferedImage = ImageIO.read(url);
            /*
             * com.google.zxing.client.j2se.BufferedImageLuminanceSource：缓冲图像亮度源
             * 将 java.awt.image.BufferedImage 转为 zxing 的 缓冲图像亮度源
             * 关键就是下面这几句：HybridBinarizer 用于读取二维码图像数据，BinaryBitmap 二进制位图
             */
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable hints = new Hashtable();
            /*
             * 如果内容包含中文，则解码的字符集格式应该和编码时一致
             */
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            /*
             * 如果图片不是二维码图片，则 decode 抛异常：com.google.zxing.NotFoundException
             * MultiFormatWriter 的 encode 用于对内容进行编码成 2D 矩阵
             * MultiFormatReader 的 decode 用于读取二进制位图数据
             */
            Result result = new MultiFormatReader().decode(bitmap, hints);
            resultStr = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("二维码图片地址错误, 地址是: {}!", url);
        } catch (NotFoundException e) {
            e.printStackTrace();
            log.error("图片非二维码图片, 地址是: {}!", url);
        }
        return resultStr;
    }


    /**
     * 输出二维码图片流
     *
     * @param text         二维码内容
     * @param width        二维码宽
     * @param height       二维码高
     * @param format       图片格式eg: png, jpg, gif
     * @param outputStream HttpServletResponse.outputStream
     *
     * @throws Exception
     */
    public static void generateQRCode(String text, int width, int height, String format, OutputStream outputStream)
            throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 指定编码格式
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 指定纠错等级
        hints.put(EncodeHintType.MARGIN, 2); // 白边大小，取值范围0~4

        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);

    }


    /**
     * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析
     */
    private static final String BASE64_IMAGE = "data:image/png;base64,%s";

    /**
     * 生成二维码，使用默认尺寸
     *
     * @param content 内容
     *
     * @return
     */
    public static String getBase64QRCode(String content) {
        return getBase64Image(content, CODE_WIDTH, CODE_HEIGHT, null, null, null);
    }

    /**
     * 生成二维码，使用默认尺寸二维码，插入默认尺寸logo
     *
     * @param content 内容
     * @param logoUrl logo地址
     *
     * @return
     */
    public static String getBase64QRCode(String content, String logoUrl) {
        return getBase64Image(content, CODE_WIDTH, CODE_HEIGHT, logoUrl, LOGO_WIDTH, LOGO_HEIGHT);
    }


    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     *
     * @return
     */
    public static String getBase64QRCode(String content, Integer width, Integer height, String logoUrl,
                                         Integer logoWidth, Integer logoHeight) {
        return getBase64Image(content, width, height, logoUrl, logoWidth, logoHeight);
    }

    private static String getBase64Image(String content, Integer width, Integer height, String logoUrl,
                                         Integer logoWidth, Integer logoHeight) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage bufferedImage = createQRCode(content, width, height, logoUrl, logoWidth, logoHeight);
        try {
            ImageIO.write(bufferedImage, IMAGE_FORMAT, os);
        } catch (IOException e) {
            log.error("[生成二维码，错误]", e);
        }
        // 转出即可直接使用
        return String.format(BASE64_IMAGE, Base64.encodeBase64(os.toByteArray()));
    }

    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     *
     * @return
     */
    public static BufferedImage createQRCode(String content, Integer width, Integer height, String logoUrl,
                                             Integer logoWidth, Integer logoHeight) {
        if (StringUtils.isNotBlank(content)) {
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>(4);
            // 指定字符编码为utf-8
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 设置图片的边距
            hints.put(EncodeHintType.MARGIN, 2);
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                if (StringUtils.isNotBlank(logoUrl)) {
                    insertLogo(bufferedImage, width, height, logoUrl, logoWidth, logoHeight);
                }
                return bufferedImage;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 二维码插入logo
     *
     * @param source     二维码
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     *
     * @throws Exception
     */
    private static void insertLogo(BufferedImage source, Integer width, Integer height, String logoUrl,
                                   Integer logoWidth, Integer logoHeight) throws Exception {
        // logo 源可为 File/InputStream/URL
        Image src = ImageIO.read(new URL(logoUrl));
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (width - logoWidth) / 2;
        int y = (height - logoHeight) / 2;
        graph.drawImage(src, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 获取二维码
     *
     * @param content 内容
     * @param output  输出流
     *
     * @throws IOException
     */
    public static void getQRCode(String content, OutputStream output) {
        BufferedImage image = createQRCode(content, CODE_WIDTH, CODE_HEIGHT, null, null, null);
        try {
            ImageIO.write(image, IMAGE_FORMAT, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取二维码
     *
     * @param content 内容
     * @param logoUrl logo资源
     * @param output  输出流
     *
     * @throws Exception
     */
    public static void getQRCode(String content, String logoUrl, OutputStream output) throws Exception {
        BufferedImage image = createQRCode(content, CODE_WIDTH, CODE_HEIGHT, logoUrl, LOGO_WIDTH, LOGO_HEIGHT);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }


}