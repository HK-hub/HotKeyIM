package com.hk.im.admin.config;

import com.hk.im.common.consntant.MinioConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * @author : HK意境
 * @ClassName : MultipartFileConfig
 * @date : 2023/3/14 9:35
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Configuration
public class MultipartFileConfig implements InitializingBean {

    @Value("${hotkey.im.file.upload.tmp-path}")
    private String tmpPath;

    @Override
    public void afterPropertiesSet() throws Exception {

        // 检查临时上传目录是否存在
        File path = new File(tmpPath);

        if (BooleanUtils.isFalse(path.exists())) {
            // 文件夹不存在创建文件
            boolean mkdirs = path.mkdirs();
            if (BooleanUtils.isFalse(mkdirs)) {
                log.warn("Could not create temp upload directory: {}", path.getAbsolutePath());
            }
        }
        log.info("上传文件临时存储目录：absolute={}, path={}", path.getAbsolutePath(), path.getPath());
    }
}
