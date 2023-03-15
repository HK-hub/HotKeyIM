package com.hk.im.infrastructure.mapper;

import com.hk.im.domain.entity.CloudResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Entity com.hk.im.domain.entity.CloudResource
 */
public interface CloudResourceMapper extends BaseMapper<CloudResource> {


    /**
     * 查询是否已经存在上传文件信息
     * @param fileName 文件名称
     * @param hash 文件hash值
     * @param md5 文件md5
     * @param size 文件大小
     * @return {@link CloudResource}
     */
    @Select("select * from tb_cloud_resource where hash=#{hash} and md5=#{md5} and `size`=#{size}" +
            " and deleted = false  limit 1")
    CloudResource existsUploadFileInfo(String fileName, @Param("hash") String hash,
                                      @Param("md5") String md5, @Param("size") Long size);

    /**
     * 增加资源引用计数
     * @param cloudResource
     * @param by
     * @return
     */
    boolean increaseResourceCount(@Param("resource") CloudResource cloudResource, @Param("by") int by);
}




