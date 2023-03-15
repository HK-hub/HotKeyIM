package com.hk.im.common.util;

import cn.hutool.core.io.file.FileNameUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Set;

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

    public static final String TEM_PATH = "temp/upload/";

    /**
     * 判断文件大小
     *
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     *
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


    /**
     * 过滤文件夹下的文件
     *
     * @param path
     * @param filter
     *
     * @return
     */
    public static File[] filterPathFiles(File path, FileFilter filter) {

        File[] files = path.listFiles(filter);
        return files;
    }

    /**
     * 获取上传文件的临时保存目录
     *
     * @param uploadId 上传文件MD5 值
     *
     * @return
     */
    public static String getUploadTempDirectory(String uploadId) {
        return TEM_PATH + uploadId;
    }


    /**
     * @author : HK意境
     * @ClassName : MergeRunnable
     * @date : 2023/3/14 15:51
     * @description : 专用于合并文件的线程执行体
     * @Todo :
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @Getter
    @Setter
    public static class MergeRunnable implements Runnable {

        // 开始写入文件位置
        private long startPos;
        // 文件分片
        private File slice;
        // 欲合并的文件名称：目标文件名称
        private String mergeFileName;

        public MergeRunnable(long startPos, File slice, String mergeFileName) {
            this.startPos = startPos;
            this.slice = slice;
            this.mergeFileName = mergeFileName;
        }

        @Override
        public void run() {

            try (RandomAccessFile randomAccessor = new RandomAccessFile(mergeFileName, "rw");
                 FileInputStream fis = new FileInputStream(slice);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(fis)) {
                // 设置开始下标
                randomAccessor.seek(startPos);
                byte[] buffer = new byte[8 * 1024];
                int len = 0;
                int offset = 0;
                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    // randomAccessor.write(buffer, offset, len);
                    randomAccessor.write(buffer);
                    offset += len;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 图片类型
     */
    public static final Set<String> imageTypes = Set.of("png", "jpg", "jpeg", "gif", "webp", "tiff", "bmp", "svg", "ico");
    // 视频类型
    public static final Set<String> videoTypes = Set.of("avi", "wmv", "mpg", "mpeg", "mov", "mp4", "rm", "ram", "m3u8", "asf");
    // 音频类型
    public static final Set<String> voiceTypes = Set.of("mp3", "wma", "wav", "aif", "aiff", "au", "ra", "ram", "mid", "rmi");
    // 压缩包
    public static final Set<String> compressTypes = Set.of("zip", "7z", "cab", "gz", "bz2", "xz", "tar", "tar.gz", "tar.bz2", "tar.xz", "rar");
    // 文本文件
    public static final Set<String> textTypes = Set.of("txt", "doc", "docs", "docx", "csv", "pdf", "asc", "docm", "md", "rtf", "rtfd", "tex", "wps", "xpdf");
    public static final Set<String> codeTypes = Set.of("c", "cpp", "java", "class", "py", "js", "html", "css", "groovy", "b", "h", "jsp", "hpp", "m", "lsp", "r", "rust",
            "asp", "shell", "bat", "cs", "dart", "e", "erl", "es6", "go", "json", "xml", "kt", "lua", "pm", "rb", "sql", "scala", "ts", "swift", "yaml", "vb");

    /**
     * 判断是否为图片
     * @param fileName
     * @return
     */
    public static boolean isImage(String fileName) {
        String suffix = FileNameUtil.getSuffix(fileName);
        return imageTypes.contains(suffix.toLowerCase());
    }

    public static boolean isVideo(String fileName) {
        String suffix = FileNameUtil.getSuffix(fileName);
        return videoTypes.contains(suffix.toLowerCase());
    }


    public static boolean isVoice(String fileName) {
        String suffix = FileNameUtil.getSuffix(fileName);
        return voiceTypes.contains(suffix.toLowerCase());
    }

    public static boolean isCompress(String fileName) {
        String suffix = FileNameUtil.getSuffix(fileName);
        return compressTypes.contains(suffix.toLowerCase());
    }

    public static boolean isText(String fileName) {
        String suffix = FileNameUtil.getSuffix(fileName);
        return textTypes.contains(suffix.toLowerCase());
    }

    public static boolean isCode(String fileName) {
        String suffix = FileNameUtil.getSuffix(fileName);
        return codeTypes.contains(suffix.toLowerCase());
    }




}
