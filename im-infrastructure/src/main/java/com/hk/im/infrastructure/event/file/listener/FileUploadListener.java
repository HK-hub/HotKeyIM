package com.hk.im.infrastructure.event.file.listener;

import cn.hutool.core.io.file.FileNameUtil;
import com.hk.im.client.service.CloudResourceService;
import com.hk.im.client.service.UserCloudResourceService;
import com.hk.im.domain.dto.FileMessageExtra;
import com.hk.im.domain.entity.CloudResource;
import com.hk.im.domain.entity.UserCloudResource;
import com.hk.im.infrastructure.event.file.event.UploadFileEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : FileUploadListener
 * @date : 2023/3/14 17:12
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class FileUploadListener {

    @Resource
    private CloudResourceService cloudResourceService;
    @Resource
    private UserCloudResourceService userCloudResourceService;

    /**
     * 处理文件上传事件：进行存盘，资源映射
     * @Bug 注意 @Async 注解导致事务失效的问题
     * @param event
     */
    @Async
    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handleFileUploadEvent(UploadFileEvent event) {
        FileMessageExtra data = event.getData();

        // 素材准备
        Long uploader = data.getUploader();
        String hash = data.getMd5();
        CloudResource cloudResource = this.cloudResourceService.existsUploadFileInfo(data.getFileName(), hash, hash, data.getSize());
        if (Objects.nonNull(cloudResource)) {
            // 云资源已经存在了，进行 count + 1
            boolean update = this.cloudResourceService.increaseResourceCount(cloudResource, 1);

        } else {
            // 云资源不存在，进行保存
            String suffix = FileNameUtil.getSuffix(data.getOriginalFileName());
            cloudResource = new CloudResource();
            cloudResource.setCount(1)
                    .setBelongId(uploader)
                    .setDirectory(StringUtils.isEmpty(suffix))
                    .setExtendType(suffix)
                    .setMd5(hash)
                    .setHash(hash)
                    .setName(data.getFileName())
                    .setSize(data.getSize())
                    .setUrl(data.getUrl())
                    .setResourceType(CloudResource.getResourceType(cloudResource.getName()));
            // 保存
            this.cloudResourceService.save(cloudResource);
        }

        // 添加资源映射
        UserCloudResource userCloudResource = new UserCloudResource()
                .setUserId(uploader)
                .setResourceId(cloudResource.getId());
        this.userCloudResourceService.save(userCloudResource);
    }


}
