package com.hk.im.common.util;

import com.hk.im.common.filter.FileNameFilter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * @author : HK意境
 * @ClassName : FileUtilTest
 * @date : 2023/3/15 10:45
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class FileUtilTest {
    String path = "F:/JavaCode/HotKeyIM/document/sql";
    String upload = "F:/JavaCode/HotKeyIM/temp/upload/ruitou45iorght";

    @Test
    public void testListFiles() {


        File file = new File(upload);
        File[] files = FileUtil.filterPathFiles(file, FileNameFilter.getInstance());

        assert files != null && files.length > 0;

        for (File file1 : files) {
            System.out.println(file1.getName());
        }

    }


}