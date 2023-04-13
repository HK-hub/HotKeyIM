package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : HK意境
 * @ClassName : UploadNoteImageRequest
 * @date : 2023/3/31 10:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class UploadNoteAnnexRequest {

    private Long noteId;

    private MultipartFile file;

}
