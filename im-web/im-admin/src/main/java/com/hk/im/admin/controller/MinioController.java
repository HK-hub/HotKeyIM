package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.service.properties.MinioProperties;
import com.hk.im.service.service.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

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
            String newFileName = System.currentTimeMillis() + "." + StringUtils.substringAfterLast(fileName, ".");
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
     * 上传文件
     * @param file
     * @param bucketName
     * @return
     */
    @PostMapping("/upload/name")
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile file, String bucketName) throws IOException {

        if (file.getSize() > 0) {
            String originalFilename = file.getOriginalFilename();
            String objectUrl = minioService.putObject(bucketName, file.getInputStream(), originalFilename);
            return ResponseResult.SUCCESS(objectUrl);
        }
        return ResponseResult.FAIL("文件不存在!");
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
    public ResponseResult getFileStatusInfo(@RequestParam("fileName") String fileName) {
        String fileStatusInfo = minioService.getFileStatusInfo(minioProperties.getBucketName(), fileName);
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
    public String getPresignedObjectUrl(@RequestParam("fileName") String fileName) {
        return minioService.getObjectUrl(minioProperties.getBucketName(), fileName);
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param response
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        try {
            InputStream fileInputStream = minioService.getObject(minioProperties.getBucketName(), fileName);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载失败");
        }
    }




}
