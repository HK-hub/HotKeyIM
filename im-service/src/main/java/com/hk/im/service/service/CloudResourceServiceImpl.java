package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.AuthorizationService;
import com.hk.im.client.service.CloudResourceService;
import com.hk.im.client.service.SplitUploadService;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.CloudResource;
import com.hk.im.domain.entity.SplitUpload;
import com.hk.im.domain.request.UploadFileInfoRequest;
import com.hk.im.domain.response.UploadFileInfoResponse;
import com.hk.im.infrastructure.mapper.CloudResourceMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : CloudResourceServiceImpl
 * @date : 2023/3/12 14:52
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class CloudResourceServiceImpl extends ServiceImpl<CloudResourceMapper, CloudResource> implements CloudResourceService {

    @Resource
    private CloudResourceMapper cloudResourceMapper;
    @Resource
    private SplitUploadService splitUploadService;
    @Value("${hotkey.im.file.upload.slice-size}")
    private Integer sliceFileSize;
    @Resource
    private AuthorizationService authorizationService;


    /**
     * 待上传文件查询文件信息
     *
     * @param request
     *
     * @return
     *
     * @TODO: 定时任务：文件上传成功，合并时发布定时任务，及时清理上传的碎片
     */
    @Override
    public ResponseResult checkUploadFileInfo(UploadFileInfoRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getFileName()) ||
                StringUtils.isEmpty(request.getMd5()) || Objects.isNull(request.getFileSize()) || StringUtils.isEmpty(request.getHash());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL().setDataAsMessage("文件上传信息错误!");
        }

        if (Objects.isNull(request.getUploaderId())) {
            request.setUploaderId(UserContextHolder.get().getId());
        }
        Long uploaderId = request.getUploaderId();
        // 素材
        UploadFileInfoResponse infoResponse = new UploadFileInfoResponse();
        String fileName = request.getFileName();
        String hash = request.getHash();
        String md5 = request.getMd5();
        // 文件大小：单位字节byte
        Long size = request.getFileSize();

        // 查询是否有已经上传过的文件了
        CloudResource cloudResource = this.cloudResourceMapper.existsUploadFileInfo(fileName, hash, md5, size);
        // 假设用户上传速度为 500KB/s , 限制100MB需要上传时间为：100 * 1024 / 500 = 5 分钟以内，为了日后扩展，这里设计为token有效期30分钟
        String uploadToken = this.authorizationService.getOrSetUserUploadToken(uploaderId);

        if (Objects.nonNull(cloudResource)) {
            // TODO 文件资源信息已经存在了，查看是否需要秒传
            infoResponse.setHash(cloudResource.getHash());
            infoResponse.setMd5(cloudResource.getMd5());
            infoResponse.setUploadId(cloudResource.getMd5());
            infoResponse.setSize(cloudResource.getSize());
            // 存在,开启秒传
            infoResponse.setExists(Boolean.TRUE);
            infoResponse.setEnableTransferBySeconds(Boolean.TRUE);
            infoResponse.setUploadToken(uploadToken);
            return ResponseResult.SUCCESS(infoResponse);
        }

        // TODO 文件资源不存在: 可能没有上传过，可能上传过，但是没有上传成功，需要进行断点续传
        // 查询文件分片上传情况
        List<SplitUpload> uploadSliceList = this.splitUploadService.getFileUploadSliceList(request);
        // 获取已经上传的分片索引
        List<Integer> splitIndexList = uploadSliceList.stream().map(SplitUpload::getSplitIndex).toList();

        // 计算分片总数
        int slices = (int) Math.ceil(1.0 * size / sliceFileSize);
        infoResponse.setSplitSize(slices)
                .setUploadId(md5)
                .setUploadedIndex(splitIndexList)
                .setUploadToken(uploadToken);

        return ResponseResult.SUCCESS(infoResponse);
    }


    /**
     * 是否存在对应资源
     * @return {@link CloudResource}
     */
    @Override
    public CloudResource existsUploadFileInfo(String fileName, String hash, String md5, Long size) {
        return this.cloudResourceMapper.existsUploadFileInfo(fileName, hash, md5, size);
    }


    /**
     * 增加云资源引用计数
     * @param cloudResource
     * @param by
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseResourceCount(CloudResource cloudResource, int by) {

        boolean update = this.cloudResourceMapper.increaseResourceCount(cloudResource, by);
        return update;
    }


}




