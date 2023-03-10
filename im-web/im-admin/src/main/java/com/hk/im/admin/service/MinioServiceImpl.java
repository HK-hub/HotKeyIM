package com.hk.im.admin.service;

import cn.hutool.core.lang.UUID;
import com.hk.im.admin.properties.MinioProperties;
import com.hk.im.admin.util.MinioUtil;
import com.hk.im.client.service.MinioService;
import com.hk.im.common.consntant.MinioConstant;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.UploadResponse;
import com.hk.im.domain.request.UploadAvatarRequest;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author : HK意境
 * @ClassName : MinioServiceImpl
 * @date : 2023/1/1 0:59
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    @Resource
    private MinioUtil minioUtil;
    @Resource
    private MinioProperties minioProperties;

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            return minioUtil.bucketExists(bucketName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void makeBucket(String bucketName) {
        try {
            minioUtil.createBucket(bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> listBucketName() {
        try {
            return minioUtil.listBucketNames();
        } catch (Exception e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }

    @Override
    public List<Bucket> listBuckets() {
        try {
            return minioUtil.getAllBuckets();
        } catch (Exception e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }

    @Override
    public boolean removeBucket(String bucketName) {
        try {
            minioUtil.removeBucket(bucketName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获取路径下文件列表
     *
     * @param bucketName 存储桶
     * @param prefix     文件名称
     * @param recursive  是否递归查找，false：模拟文件夹结构查找
     *
     * @return 二进制流
     */
    @Override
    public Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive) {
        return minioUtil.listObjects(bucketName, prefix, recursive);
    }

    @Override
    public List<String> listObjectNames(String bucketName) {
        try {
            return minioUtil.listObjectNames(bucketName);
        } catch (Exception e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }


    /**
     * 上传文件
     *
     * @param inputStream
     * @param bucketName
     * @param objectName
     * @param fileType
     *
     * @return
     */
    @Override
    public String putObject(InputStream inputStream, String bucketName, String objectName, String fileType) throws Exception {
        if (!this.bucketExists(bucketName)) {
            this.makeBucket(bucketName);
        }
        minioUtil.uploadFile(bucketName, inputStream, objectName, fileType);
        return minioProperties.getConsole() + "/" + bucketName + "/" + objectName;
    }


    /**
     * 上传文件
     * @param inputStream
     * @param bucketName
     * @param objectName
     * @return
     */
    @Override
    public String putObject(InputStream inputStream, String bucketName, String objectName) {
        try {
            if (!this.bucketExists(bucketName)) {
                this.makeBucket(bucketName);
            }
            minioUtil.uploadFile(bucketName, objectName, inputStream);
            return minioProperties.getConsole() + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 上传文件无需指定objectName, 内部会重写ObjectName
     * @param bucketName
     * @param inputStream
     * @param originalFilename
     * @return
     */
    @Override
    public String putObject(String bucketName, InputStream inputStream, String originalFilename) {
        try {
            if (!this.bucketExists(bucketName)) {
                this.makeBucket(bucketName);
            }
            long now = System.currentTimeMillis() / 1000;
            String fileName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + now + "_" + new Random().nextInt(1000)
                    + originalFilename.substring(originalFilename.lastIndexOf("."));

            String url = this.putObject(inputStream, bucketName, fileName);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 上传视频
     *
     * @param inputStream
     * @param bucketName
     * @param originalFilename
     *
     * @return
     *
     * @throws Exception
     */
    @Override
    public UploadResponse uploadVideo(InputStream inputStream, String bucketName, String originalFilename) {
        try {
            UploadResponse uploadResponse = this.minioUtil.uploadVideo(inputStream, bucketName, originalFilename);
            return uploadResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 下载文件
     *
     * @param bucketName
     * @param objectName
     *
     * @return
     */
    @Override
    public InputStream downloadObject(String bucketName, String objectName) {
        try {
            return minioUtil.getObject(bucketName, objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 断点下载
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     *
     * @return 二进制流
     */
    @Override
    public InputStream downloadObject(String bucketName, String objectName, long offset, long length) {
        try {
            return minioUtil.getObject(bucketName, objectName, offset, length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取文件流
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     *
     * @return 二进制流
     */
    @Override
    public InputStream getObject(String bucketName, String objectName) {
        try {
            return minioUtil.getObject(bucketName, objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectName
     *
     * @return
     */
    @Override
    public boolean removeObject(String bucketName, String objectName) {
        try {
            minioUtil.removeFile(bucketName, objectName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 批量删除文件
     *
     * @param bucketName
     * @param objectNameList
     *
     * @return
     */
    @Override
    public boolean removeListObject(String bucketName, List<String> objectNameList) {
        try {
            minioUtil.removeFiles(bucketName, objectNameList);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件外链
     *
     * @param bucketName
     * @param objectName
     *
     * @return
     */
    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        try {
            return minioUtil.getPresignedObjectUrl(bucketName, objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 上传头像
     * @param request
     * @return
     */
    @Override
    public ResponseResult uploadAvatar(UploadAvatarRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getTargetId()) || Objects.isNull(request.getFile()) || Objects.isNull(request.getType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 上传路径
        String bucket = null;
        String objectName = null;
        String url = null;

        // 判断头像类型
        UploadAvatarRequest.AvatarType type = UploadAvatarRequest.AvatarType.values()[request.getType()];
        if (type == UploadAvatarRequest.AvatarType.USER) {
            // 用户头像
            bucket = MinioConstant.BucketEnum.User.getBucket();
            objectName = MinioConstant.getUserAvatarPath(request.getTargetId());
        } else {
            // 群聊头像
            bucket = MinioConstant.BucketEnum.Group.getBucket();
            objectName = MinioConstant.getGroupAvatarPath(request.getTargetId());
        }

        // 上传头像
        try {
            url = this.putObject(request.getFile().getInputStream(), bucket, objectName);
        } catch (IOException e) {
            // 异常失败
            return ResponseResult.FAIL(e.getMessage());
        }

        if (Objects.isNull(url)) {
            // 上传失败
            return ResponseResult.FAIL("上传头像失败!");
        }

        // 上传成功
        return ResponseResult.SUCCESS(url);
    }


    /**
     * 上传用户聊天图片
     * @param image
     * @param bucket
     * @param senderId
     * @return
     */
    @Override
    public String putImage(MultipartFile image, String bucket, Long senderId) {

        // 扩展名
        String extension = FilenameUtils.getExtension(image.getOriginalFilename());
        // 计算名称
        String objectName = MinioConstant.getPrivateImagePath(
                UUID.fastUUID().toString(true) + "." + extension);
        // 上传成功链接
        String url = null;
        try {
           url = this.putObject(image.getInputStream(), bucket, objectName);
        } catch (IOException e) {
            log.error("putImage error", e);
        }
        return url;
    }


    /**
     * 获取文件信息, 如果抛出异常则说明文件不存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     *
     * @return
     */
    @Override
    public String getFileStatusInfo(String bucketName, String objectName) {
        String info = null;
        try {
            info = minioUtil.getFileStatusInfo(bucketName, objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }


}