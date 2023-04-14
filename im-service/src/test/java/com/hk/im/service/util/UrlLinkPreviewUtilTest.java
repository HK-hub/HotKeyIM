package com.hk.im.service.util;

import com.hk.im.domain.bo.PreviewLinkBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : HK意境
 * @ClassName : UrlLinkPreviewUtilTest
 * @date : 2023/4/14 22:21
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
class UrlLinkPreviewUtilTest {

    @Test
    void scrapeLink() throws IOException {

        PreviewLinkBO previewLinkBO = UrlLinkPreviewUtil.scrapeLink("https://mediamask.io/for-developers#feature-block");
        log.info("{}",previewLinkBO);

    }
}