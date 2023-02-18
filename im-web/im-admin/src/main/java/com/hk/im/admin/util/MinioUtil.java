package com.hk.im.admin.util;

import com.hk.im.admin.properties.MinioProperties;
import com.hk.im.common.resp.UploadResponse;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author : HK意境
 * @ClassName : MinioUtil
 * @date : 2023/1/1 0:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;
    @Resource
    private MinioProperties minioProperties;

    // 最大文件限制
    private final Long maxSize = (long) (1024 * 1024);


    public void createBucket(String bucketName) throws Exception {
        if (!bucketExists(bucketName)) {
            try {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断Bucket是否存在，true：存在，false：不存在
     *
     * @param bucketName 名称
     *
     * @return boolean
     */
    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }


    /**
     * 获得Bucket的策略
     *
     * @param bucketName
     *
     * @return
     */

    public String getBucketPolicy(String bucketName) throws Exception {
        return minioClient.getBucketPolicy(GetBucketPolicyArgs
                .builder()
                .bucket(bucketName)
                .build());
    }

    /**
     * 获得所有Bucket列表
     *
     * @return
     */

    public List<Bucket> getAllBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 列出所有存储桶名称
     *
     * @return
     */
    public List<String> listBucketNames() throws Exception {
        List<Bucket> bucketList = getAllBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 根据bucketName获取其相关信息
     *
     * @param bucketName
     *
     * @return
     */

    public Optional<Bucket> getBucket(String bucketName) throws Exception {
        return getAllBuckets().stream().
                filter(b -> b.name().equals(bucketName))
                .findFirst();
    }


    /**
     * 根据bucketName删除Bucket，true：删除成功； false：删除失败，文件或已不存在
     *
     * @param bucketName
     *
     * @throws Exception
     */

    public void removeBucket(String bucketName) throws Exception {
        minioClient.removeBucket(
                RemoveBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
    }


    /**
     * 判断文件是否存在
     *
     * @param bucketName
     * @param objectName
     *
     * @return
     */
    public boolean isObjectExist(String bucketName, String objectName) throws Exception {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            log.error("[Minio工具类]>>>> 判断文件是否存在, 异常：", e);
            exist = false;
        }
        return exist;
    }


    /**
     * 判断文件夹是否存在
     *
     * @param bucketName
     * @param objectName
     *
     * @return
     */
    public boolean isFolderExist(String bucketName, String objectName) throws Exception {
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).prefix(objectName).recursive(false).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir() && objectName.equals(item.objectName())) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            log.error("[Minio工具类]>>>> 判断文件夹是否存在，异常：", e);
            exist = false;
        }
        return exist;
    }


    /**
     * 根据文件前缀查询文件
     *
     * @param bucketName 存储桶
     * @param prefix     前缀
     * @param recursive  是否使用递归查询
     *
     * @return MinioItem 列表
     */

    public List<Item> getAllObjectsByPrefix(String bucketName,
                                            String prefix,
                                            boolean recursive) throws Exception {
        List<Item> list = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName)
                        .prefix(prefix)
                        .recursive(recursive)
                        .build());
        if (objectsIterator != null) {
            for (Result<Item> o : objectsIterator) {
                Item item = o.get();
                list.add(item);
            }
        }
        return list;
    }


    /**
     * 以流的形式获取一个文件对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     *
     * @return
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                InputStream stream =
                        minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(bucketName)
                                        .object(objectName)
                                        .build());
                return stream;
            }
        }
        return null;
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

    public InputStream getObject(String bucketName, String objectName, long offset, long length) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .offset(offset)
                        .length(length)
                        .build());
    }


    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     *
     * @return
     */
    public StatObjectResponse statObject(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse stat =
                    minioClient.statObject(
                            StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return stat;
        }
        return null;
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
    public Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .recursive(recursive)
                        .build());
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     *
     * @return
     */
    public List<String> listObjectNames(String bucketName) throws Exception {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        } else {
            listObjectNames.add("存储桶不存在");
        }
        return listObjectNames;
    }


    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     *
     * @return
     */
    public Iterable<Result<Item>> listObjects(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }


    /**
     * 使用MultipartFile进行文件上传
     *
     * @param bucketName  存储桶
     * @param in          文件输入流
     * @param objectName  对象名
     * @param contentType 类型
     *
     * @return
     */
    public ObjectWriteResponse uploadFile(String bucketName, InputStream in, String objectName, String contentType) throws Exception {

        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .contentType(contentType)
                        .stream(in, in.available(), -1)
                        .build());
    }


    /**
     * 通过InputStream上传对象
     *
     * @param bucketName  存储桶名称
     * @param objectName  存储桶里的对象名称
     * @param inputStream 要上传的流
     * @param contentType 要上传的文件类型 MimeTypeUtils.IMAGE_JPEG_VALUE
     *
     * @return
     */
    public boolean putObject(String bucketName, String objectName, InputStream inputStream, String contentType) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                    inputStream, -1, maxSize)
                            .contentType(contentType)
                            .build());
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 图片上传
     *
     * @param bucketName
     * @param inputStream
     * @param imageName
     *
     * @return
     */
    public ObjectWriteResponse uploadImage(String bucketName, InputStream inputStream, String imageName) throws Exception {
        return uploadFile(bucketName, imageName, inputStream);
    }


    /**
     * 上传本地文件
     *
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param filePath   本地文件路径
     *
     * @return
     */
    public ObjectWriteResponse uploadFile(String bucketName, String objectName, String filePath) throws Exception {
        return minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filePath)
                        .build());
    }


    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     *
     * @return
     */
    public ObjectWriteResponse uploadFile(String bucketName, String objectName, InputStream inputStream) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
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
    public UploadResponse uploadVideo(InputStream inputStream, String bucketName, String originalFilename) throws Exception {
        //判断文件是否为空
        if (null == inputStream || 0 == inputStream.available()) {
            return null;
        }

        //判断存储桶是否存在  不存在则创建
        createBucket(bucketName);
        //文件名
        //新的文件名 = 时间戳_随机数.后缀名
        assert originalFilename != null;
        long now = System.currentTimeMillis() / 1000;
        String fileName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + now + "_" + new Random().nextInt(1000)
                + originalFilename.substring(originalFilename.lastIndexOf("."));
        //开始上传
        log.info("file大小:{}", inputStream.available());
        this.uploadFile(bucketName, inputStream, fileName, MimeTypeEnum.VIDEO.mimeType);

        String minioUrl = minioProperties.getConsole() + "/" + bucketName + "/" + fileName;
        String nginxUrl = minioProperties.getNginx() + "/" + bucketName + "/" + fileName;
        return new UploadResponse(minioUrl, nginxUrl);
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     *
     * @return
     */
    public ObjectWriteResponse createDir(String bucketName, String objectName) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }


    /**
     * 获取文件信息, 如果抛出异常则说明文件不存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     *
     * @return
     */
    public String getFileStatusInfo(String bucketName, String objectName) throws Exception {
        return minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()).toString();
    }


    /**
     * 拷贝文件
     *
     * @param bucketName    存储桶
     * @param objectName    文件名
     * @param srcBucketName 目标存储桶
     * @param srcObjectName 目标文件名
     */
    public ObjectWriteResponse copyFile(String bucketName, String objectName, String srcBucketName, String srcObjectName) throws Exception {
        return minioClient.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder().bucket(bucketName).object(objectName).build())
                        .bucket(srcBucketName)
                        .object(srcObjectName)
                        .build());
    }


    /**
     * 删除文件
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     */
    public void removeFile(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }


    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶
     * @param keys       需要删除的文件列表
     *
     * @return
     */
    public void removeFiles(String bucketName, List<String> keys) {
        List<DeleteObject> objects = new LinkedList<>();
        keys.forEach(s -> {
            objects.add(new DeleteObject(s));
            try {
                removeFile(bucketName, s);
            } catch (Exception e) {
                log.error("[Minio工具类]>>>> 批量删除文件，异常：", e);
            }
        });
    }


    /**
     * 获取文件外链
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     * @param expires    过期时间 <=7 秒 （外链有效时间（单位：秒））
     *
     * @return url
     */
    public String getPreSignedObjectUrl(String bucketName, String objectName, Integer expires) throws Exception {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .expiry(expires)
                .bucket(bucketName)
                .object(objectName)
                .build();
        return minioClient.getPresignedObjectUrl(args);
    }

    /**
     * 获得文件外链
     *
     * @param bucketName
     * @param objectName
     *
     * @return url
     */
    public String getPresignedObjectUrl(String bucketName, String objectName) throws Exception {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(args);
    }


    /**
     * 将URLDecoder编码转成UTF8
     *
     * @param str
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public String getUtf8ByURLDecoder(String str) throws UnsupportedEncodingException {
        String url = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }


    public enum MimeTypeEnum {
        VIDEO("video/mp4");

        MimeTypeEnum(String mimeType) {
            this.mimeType = mimeType;
        }

        private String mimeType;


    }

}
