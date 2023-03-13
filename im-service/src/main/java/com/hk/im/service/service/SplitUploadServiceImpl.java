package com.hk.im.service.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.SplitUploadService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.SplitUpload;
import com.hk.im.domain.request.MergeSplitFileRequest;
import com.hk.im.domain.request.SecondsTransferRequest;
import com.hk.im.domain.request.SplitUploadRequest;
import com.hk.im.domain.request.UploadFileInfoRequest;
import com.hk.im.infrastructure.mapper.SplitUploadMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @ClassName : SplitUploadServiceImpl
 * @author : HK意境
 * @date : 2023/3/10 17:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class SplitUploadServiceImpl extends ServiceImpl<SplitUploadMapper, SplitUpload> implements SplitUploadService {

    @Value("${spring.servlet.multipart.slice-file-size}")
    private Integer sliceFileSize;

    /**
     * 分片上传聊天消息文件
     * @param request
     * @return
     */
    @Override
    public ResponseResult uploadTalkFile(SplitUploadRequest request) {
        return null;
    }

    /**
     * 合并文件
     * @param request
     * @return
     */
    @Override
    public ResponseResult mergeSplitUploadFile(MergeSplitFileRequest request) {
        return null;
    }

    /**
     * 秒传文件
     * @param request
     * @return
     */
    @Override
    public ResponseResult transferFileBySeconds(SecondsTransferRequest request) {
        return null;
    }


    /**
     * 获取上传文件已经上传了的分片列表
     * @param request
     * @return {@link List<SplitUpload>}
     */
    @Override
    public List<SplitUpload> getFileUploadSliceList(UploadFileInfoRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getMd5()) || StringUtils.isEmpty(request.getFileName());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return Collections.emptyList();
        }

        if (Objects.isNull(request.getUploaderId())) {
            request.setUploaderId(UserContextHolder.get().getId());
        }

        // 查询已经上传的分片
        List<SplitUpload> splitUploadList = this.lambdaQuery()
                .eq(SplitUpload::getUserId, request.getUploaderId())
                .eq(SplitUpload::getUploadId, request.getMd5())
                .eq(SplitUpload::getOriginalName, request.getFileName())
                .eq(SplitUpload::getIsDelete, Boolean.FALSE)
                .list();

        // 根据 spit_index 去重
        splitUploadList = splitUploadList.stream()
                .filter(distinctByKey(SplitUpload::getSplitIndex))
                .toList();

        return splitUploadList;
    }

    /**
     * 根据key 去重
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}




