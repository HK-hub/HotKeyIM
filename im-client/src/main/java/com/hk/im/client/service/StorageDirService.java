package com.hk.im.client.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.entity.StorageDir;


/**
 * @ClassName : StorageDirService
 * @author : HK意境
 * @date : 2023/4/10 16:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface StorageDirService extends IService<StorageDir> {

    StorageDir getUserDir(Long id);

    int setUserDir(StorageDir userDir);

    boolean deleteStruct(Long userId, String url);
}
