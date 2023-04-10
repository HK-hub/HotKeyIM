package com.hk.im.client.service;

import com.hk.im.domain.entity.CloudFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName : OssService
 * @author : HK意境
 * @date : 2023/4/10 17:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface OssService {
    CloudFile upload(MultipartFile file, String catalogue);

    String delete(String id);

    String uploadfile(MultipartFile file);

    String deleteVideo(String id);

}
