package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.admin.properties.MinioProperties;
import com.hk.im.domain.request.UploadAvatarRequest;
import com.hk.im.service.service.MinioService;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MinioController
 * @date : 2023/1/1 12:58
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/minio")
public class MinioController {

    @Resource
    private MinioService minioService;
    @Resource
    private MinioProperties minioProperties;

    /**
     * 文件上传
     *
     * @param file
     */
    @PostMapping("/upload/file")
    public ResponseResult upload(@RequestParam("file") MultipartFile file) {
        try {
            //文件名
            String fileName = file.getOriginalFilename();
            String newFileName = System.currentTimeMillis() + "." +
                    StringUtils.substringAfterLast(fileName, ".");
            //类型
            String contentType = file.getContentType();
            // 返回文件外链
            String objectUrl = this.minioService.putObject(file.getInputStream(),
                    minioProperties.getBucketName(), newFileName, contentType);
            return ResponseResult.SUCCESS(objectUrl).setMessage("上传成功!");
        } catch (Exception e) {
            log.error("上传失败");
            return ResponseResult.FAIL("文件:"+file.getOriginalFilename() + " 上传失败!");
        }
    }


    /**
     * 上传头像
     * @param request
     * @return
     */
    @PostMapping("/upload/avatar")
    public ResponseResult uploadAvatar(UploadAvatarRequest request) {

        return this.minioService.uploadAvatar(request);
    }

    /**
     * 上传文件
     * @param file
     * @param bucketName
     * @return
     */
    @PostMapping("/upload/name")
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("bucket") String bucketName) throws IOException {

        if (file.getSize() > 0) {
            String originalFilename = file.getOriginalFilename();
            String objectUrl = minioService.putObject(bucketName, file.getInputStream(), originalFilename);
            return ResponseResult.SUCCESS(objectUrl);
        }
        return ResponseResult.FAIL("文件不存在!");
    }


    /**
     * 获取所有 bucket
     * @return
     */
    @GetMapping("/bucket/list")
    public ResponseResult getBuckets() {
        List<Bucket> buckets = this.minioService.listBuckets();
        if (CollectionUtils.isEmpty(buckets)) {
            buckets = Collections.emptyList();
        }
        return ResponseResult.SUCCESS(buckets);
    }


    /**
     * 获取bucket下所有文件
     * @param bucket
     * @return
     */
    @GetMapping("/files")
    public ResponseResult getBucketAllFile(@RequestParam("bucket") String bucket) {
        List<String> fileNames = this.minioService.listObjectNames(bucket);
        return ResponseResult.SUCCESS(fileNames);
    }

    /**
     * 删除
     *
     * @param fileName
     */
    @DeleteMapping("/")
    public ResponseResult delete(@RequestParam("fileName") String fileName) {
        boolean res = minioService.removeObject(minioProperties.getBucketName(), fileName);
        if (BooleanUtils.isTrue(res)) {
            return ResponseResult.SUCCESS(fileName + " 删除成功!");
        }
        return ResponseResult.SUCCESS(fileName + " 删除失败!");

    }

    /**
     * 获取文件信息
     *
     * @param fileName
     * @return
     */
    @GetMapping("/info")
    public ResponseResult getFileStatusInfo(@RequestParam("fileName") String fileName,
                                            @RequestParam(value = "bucket", required = false) String bucket) {
        // 获取 bucket
        if (StringUtils.isEmpty(bucket)) {
            bucket = minioProperties.getBucketName();
        }

        String fileStatusInfo = minioService.getFileStatusInfo(bucket, fileName);
        if (StringUtils.isEmpty(fileStatusInfo)) {
            return ResponseResult.FAIL("文件：" + fileName +  "不存在");
        }
        return ResponseResult.SUCCESS(fileStatusInfo);
    }

    /**
     * 获取文件外链
     *
     * @param fileName
     * @return
     */
    @GetMapping("/url")
    public String getPresignedObjectUrl(@RequestParam("fileName") String fileName,
                                        @RequestParam(value = "bucket", required = false) String bucket) {

        // 获取 bucket
        if (StringUtils.isEmpty(bucket)) {
            bucket = minioProperties.getBucketName();
        }

        return minioService.getObjectUrl(bucket, fileName);
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param response
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName,
                         @RequestParam(value = "bucket", required = false) String bucket,
                         HttpServletResponse response) {
        try {
            // 获取 bucket
            if (StringUtils.isEmpty(bucket)) {
                bucket = minioProperties.getBucketName();
            }
            InputStream fileInputStream = minioService.getObject(bucket, fileName);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载失败");
        }
    }

}
