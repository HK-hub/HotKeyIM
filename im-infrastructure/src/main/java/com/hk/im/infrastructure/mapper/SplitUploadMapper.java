package com.hk.im.infrastructure.mapper;

import com.hk.im.domain.entity.SplitUpload;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.hk.im.domain.entity.SplitUpload
 */
public interface SplitUploadMapper extends BaseMapper<SplitUpload> {

    /**
     * 查询上传分片是否存在并且获取
     * @param uploadId
     * @param splitIndex
     * @param splitNum
     * @return
     */
    SplitUpload selectTheUploadSlice(@Param("uploadId") String uploadId, @Param("splitIndex") Integer splitIndex, @Param("splitNum") Integer splitNum);

}




