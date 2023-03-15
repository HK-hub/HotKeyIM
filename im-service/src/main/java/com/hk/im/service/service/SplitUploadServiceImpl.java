package com.hk.im.service.service;


import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.AuthorizationService;
import com.hk.im.client.service.MinioService;
import com.hk.im.client.service.SplitUploadService;
import com.hk.im.common.consntant.MinioConstant;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.filter.FileNameFilter;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.util.FileUtil;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.SplitUpload;
import com.hk.im.domain.request.MergeSplitFileRequest;
import com.hk.im.domain.request.SecondsTransferRequest;
import com.hk.im.domain.request.SplitUploadRequest;
import com.hk.im.domain.request.UploadFileInfoRequest;
import com.hk.im.infrastructure.mapper.SplitUploadMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author : HK意境
 * @ClassName : SplitUploadServiceImpl
 * @date : 2023/3/10 17:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class SplitUploadServiceImpl extends ServiceImpl<SplitUploadMapper, SplitUpload> implements SplitUploadService {

    @Value("${hotkey.im.file.upload.tmp-path}")
    private String tmpPath;
    @Value("${hotkey.im.file.upload.slice-size}")
    private Integer sliceFileSize;
    @Resource
    private SplitUploadMapper splitUploadMapper;
    @Resource
    private AuthorizationService authorizationService;
    @Resource(name = "mergeSliceFileThreadPool")
    private ThreadPoolExecutor mergeSliceFileThreadPool;
    @Resource
    private MinioService minioService;


    /**
     * 分片上传聊天消息文件
     *
     * @param request
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult uploadTalkFile(SplitUploadRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getUploadId()) || StringUtils.isEmpty(request.getToken())
                || Objects.isNull(request.getSplitIndex()) || Objects.isNull(request.getSplitNum()) || Objects.isNull(request.getFile());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL().setDataAsMessage("文件上传失败,请稍后重试!");
        }

        // 校验临时上传token凭证
        String token = request.getToken();
        Long uploader = UserContextHolder.get().getId();
        String authToken = this.authorizationService.getAuthCode(RedisConstants.UPLOAD_TOKEN + uploader);
        if (!StringUtils.equals(token, authToken)) {
            // 上传凭证错误
            return ResponseResult.FAIL("抱歉您的文件上传凭证已经失效了!");
        }

        // 检查数据库是否存在对应分片信息
        Integer index = request.getSplitIndex();
        String hash = request.getUploadId();
        SplitUpload uploadSlice = this.splitUploadMapper.selectTheUploadSlice(hash, request.getSplitIndex(),
                request.getSplitNum());
        // 查询临时文件目录下是否存在分片信息
        File temp = new File(tmpPath, hash);
        File[] slices = FileUtil.filterPathFiles(temp, FileNameFilter.getInstance());
        if (slices.length > index && Objects.nonNull(slices[index])) {
            // 分片存在了: 直接返回
            return ResponseResult.SUCCESS();
        }

        // 分片不存在: 保存分片到临时上传目录，数据库
        String slicePath = temp.getAbsolutePath() + "/" + index;
        ResponseResult result = this.saveSliceFileToTempDirectory(request, slicePath);
        // 分片写入临时目录结果
        if (BooleanUtils.isFalse((result.isSuccess()))) {
            // 写入失败
            request.setFile(null).setToken(null);
            return ResponseResult.FAIL(request).setMessage("分片文件保存失败!");
        }

        // 写入磁盘成功,准备写入数据库分片信息
        SplitUpload sliceUploadFile = this.buildUploadSliceFile(request, slicePath);
        boolean save = this.save(sliceUploadFile);
        if (BooleanUtils.isFalse(save)) {
            // 写入数据库失败
            return ResponseResult.FAIL().setMessage("分片文件写入失败!");
        }

        // 写入临时目录和数据库都成功
        return ResponseResult.SUCCESS();
    }


    /**
     * 保存分片到临时目录：如果磁盘已经存在了该分片则进行覆盖
     *
     * @param request
     */
    private ResponseResult saveSliceFileToTempDirectory(SplitUploadRequest request, String temp) {

        MultipartFile file = request.getFile();
        log.info("start to write slice file to temp directory: {}", temp);

        // 判断分片是否存在了
        File slice = new File(temp);
        boolean exists = slice.exists() && slice.isFile() && slice.length() == file.getSize();
        if (BooleanUtils.isTrue(exists)) {
            // 文件分片已经存在了，可以不用上传
            return ResponseResult.SUCCESS(temp);
        }

        // 使用缓冲区
        try (BufferedInputStream bufferedIn = new BufferedInputStream(file.getInputStream());
             BufferedOutputStream bufferedOut = new BufferedOutputStream(new FileOutputStream(temp))) {
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = bufferedIn.read(buffer)) != -1) {
                bufferedOut.write(buffer, 0, len);
            }
            // 刷盘
            bufferedOut.flush();
        } catch (Exception e) {

            log.error("write slice file to temp directory error: ", e);
            return ResponseResult.FAIL();
        }
        // 响应
        return ResponseResult.SUCCESS(temp);
    }

    /**
     * 构建上传的文件分片为SpitUpload 保存到数据库
     *
     * @param request
     *
     * @return
     */
    private SplitUpload buildUploadSliceFile(SplitUploadRequest request, String path) {

        MultipartFile file = request.getFile();
        SplitUpload splitUpload = new SplitUpload()
                .setUploadId(request.getUploadId())
                .setUserId(UserContextHolder.get().getId())
                .setType(SplitUpload.FileType.SPLIT.ordinal())
                .setFileExt(FilenameUtils.getExtension(request.getOriginalFileName()))
                .setFileSize((int) file.getSize())
                .setSplitIndex(request.getSplitIndex())
                .setSplitNum(request.getSplitNum())
                .setOriginalName(request.getOriginalFileName())
                .setPath(path);

        return splitUpload;
    }


    /**
     * 合并文件
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult mergeSplitUploadFile(MergeSplitFileRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getMd5());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 获取所有分片文件，进行合并
        String hash = request.getUploadId();

        // String tempDirectory = FileUtil.getUploadTempDirectory(uploadId);

        // 获取所有分片
        File[] slices = FileUtil.filterPathFiles(new File(tmpPath, hash), FileNameFilter.getInstance());
        // 检查分片是否正确
        Integer size = request.getSize();
        int sliceSize = (int) Math.ceil(1.0 * size / sliceFileSize);
        if (sliceSize != slices.length) {
            // 分片数量不够，未上传完成
            return ResponseResult.FAIL().setMessage("文件分片不完整,无法合并!");
        }

        // 转换为List: 按照分片名称排序
        List<File> sliceList = Arrays.stream(slices)
                .sorted(Comparator.comparing(file -> Integer.valueOf(file.getName())))
                .toList();

        // 进行合并
        String mergeFilePath = null;
        CompletableFuture[] futures = new CompletableFuture[sliceList.size()];
        Path path = Paths.get(tmpPath, hash, hash, FilenameUtils.getExtension(request.getFileName()));
        try {
            // 创建文件
            Path mergeFile = Files.createFile(path);
            mergeFilePath = mergeFile.toFile().getAbsolutePath();
            int index = 0;
            int offset = 0;
            // 异步合并
            for (File file : sliceList) {
                futures[index] = CompletableFuture.runAsync(new FileUtil.MergeRunnable(offset, file, mergeFilePath),
                        this.mergeSliceFileThreadPool);
                index++;
                offset += file.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 合并失败
            return ResponseResult.FAIL();
        }

        // 全部合并
        try {
            CompletableFuture.allOf(futures).get();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.FAIL();
        }

        // 合并成
        return ResponseResult.SUCCESS(new File(mergeFilePath));
    }

    /**
     * 秒传文件
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult transferFileBySeconds(SecondsTransferRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getMd5()) || Objects.isNull(request.getReceiverId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL();
        }
        String fileName = request.getFileName();
        String md5 = request.getMd5();

        return null;
    }


    /**
     * 获取上传文件已经上传了的分片列表
     *
     * @param request
     *
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
     *
     * @param keyExtractor
     * @param <T>
     *
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}




