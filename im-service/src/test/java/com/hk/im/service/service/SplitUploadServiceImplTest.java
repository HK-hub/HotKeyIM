package com.hk.im.service.service;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : HK意境
 * @ClassName : SplitUploadServiceImplTest
 * @date : 2023/3/14 14:47
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class SplitUploadServiceImplTest {

    @Test
    public void testFileOver() throws FileNotFoundException {

        String absolutePath = ResourceUtils.getFile("classpath:temp/upload").getAbsolutePath();
        File file = new File(absolutePath + "1.txt");

    }


}