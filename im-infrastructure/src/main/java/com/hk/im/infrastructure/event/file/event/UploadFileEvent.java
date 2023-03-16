package com.hk.im.infrastructure.event.file.event;

import com.hk.im.domain.dto.FileMessageExtra;
import com.hk.im.infrastructure.event.AbstractEvent;


/**
 * @author : HK意境
 * @ClassName : UploadFileEvent
 * @date : 2023/3/14 17:09
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UploadFileEvent extends AbstractEvent<FileMessageExtra> {
    public UploadFileEvent(Object source) {
        super(source);
    }

    public UploadFileEvent(Object source, FileMessageExtra data) {
        super(source);
        this.data = data;
    }


}
