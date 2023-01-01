package com.hk.im.admin.service;

import com.hk.im.admin.properties.MinioProperties;
import com.hk.im.admin.util.MinioUtil;
import com.hk.im.common.resp.UploadResponse;
import com.hk.im.service.service.MinioService;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    public String putObject(InputStream inputStream, String bucketName, String objectName, String fileType) {
        try {
            if (!this.bucketExists(bucketName)) {
                this.makeBucket(bucketName);
            }
            minioUtil.uploadFile(bucketName, inputStream, objectName, fileType);
            return minioProperties.getEndpoint() + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
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
            return minioProperties.getEndpoint() + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
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