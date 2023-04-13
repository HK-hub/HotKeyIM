package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.entity.CloudFile;

import java.util.List;


/**
 * @ClassName : CloudFileService
 * @author : HK意境
 * @date : 2023/4/10 16:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface CloudFileService extends IService<CloudFile> {

    List<CloudFile> getAllFileInfo(String memId);

    List<CloudFile> getFileInfo(String id);

    List<CloudFile> getCurFiles(String userDir,String id);

    CloudFile getFiles(String id);

    List<CloudFile> getFindFile(Long userId,String name);

    List<CloudFile> getList(Long userId, String url,int result,String name);
}
