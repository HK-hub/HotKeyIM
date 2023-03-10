package com.hk.im.common.util;

/**
 * @author : HK意境
 * @ClassName : FileUtil
 * @date : 2023/3/10 8:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class FileUtil {

    /**
     * 判断文件大小
     *
     * @param len
     *            文件长度
     * @param size
     *            限制大小
     * @param unit
     *            限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {

        double fileSize = switch (unit.toUpperCase()) {
            case "B" -> (double) len;
            case "K" -> (double) len / 1024;
            case "M" -> (double) len / 1048576;
            case "G" -> (double) len / 1073741824;
            default -> 0;
        };
        return !(fileSize > size);
    }



}
