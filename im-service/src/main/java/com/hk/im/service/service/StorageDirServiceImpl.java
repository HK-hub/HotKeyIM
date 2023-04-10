package com.hk.im.service.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hk.im.client.service.CloudFileService;
import com.hk.im.client.service.StorageDirService;
import com.hk.im.domain.entity.CloudFile;
import com.hk.im.domain.entity.StorageDir;
import com.hk.im.infrastructure.mapper.StorageDirMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName : StorageDirServiceImpl
 * @author : HK意境
 * @date : 2023/4/10 22:26
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class StorageDirServiceImpl extends ServiceImpl<StorageDirMapper, StorageDir> implements StorageDirService {
    @Resource
    private CloudFileService cloudFileService;

    @Override
    public StorageDir getUserDir(Long userId) {
        return this.getById(userId);
    }

    @Override
    public int setUserDir(StorageDir userDir) {
        return baseMapper.updateById(userDir);
    }

    @Override
    public boolean deleteStruct(Long userId, String url) {
        boolean remove = this.cloudFileService.lambdaUpdate()
                .eq(CloudFile::getUserId, userId)
                .like(CloudFile::getFDir, url)
                .remove();
        return remove;
    }

}
