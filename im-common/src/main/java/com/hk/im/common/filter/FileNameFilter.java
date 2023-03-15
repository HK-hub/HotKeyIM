package com.hk.im.common.filter;

import org.apache.commons.lang3.BooleanUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : HK意境
 * @ClassName : FileNameFilter
 * @date : 2023/3/14 10:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class FileNameFilter implements FileFilter {

    private static final FileNameFilter INSTANCE = new FileNameFilter();


    // 0 或 正整数
    private final Pattern pattern = Pattern.compile("\\d+");


    @Override
    public boolean accept(File file) {

        // 是否文件
        if (BooleanUtils.isFalse(file.isFile())) {
            return false;
        }
        // 文件名为分片数字
        String name = file.getName();
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            // 名字不匹配
            return false;
        }

        return true;
    }


    public static FileNameFilter getInstance() {
        return INSTANCE;
    }

}
