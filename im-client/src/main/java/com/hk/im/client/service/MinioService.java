package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.UploadResponse;
import com.hk.im.domain.request.UploadAvatarRequest;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MinioService
 * @date : 2023/1/1 0:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface MinioService {

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName
     *
     * @return
     */
    boolean bucketExists(String bucketName);

    /**
     * 创建 bucket
     *
     * @param bucketName
     */
    void makeBucket(String bucketName);

    /**
     * 列出所有存储桶名称
     *
     * @return
     */
    List<String> listBucketName();

    /**
     * 列出所有存储桶 信息
     *
     * @return
     */
    List<Bucket> listBuckets();

    /**
     * 根据桶名删除桶
     *
     * @param bucketName
     */
    boolean removeBucket(String bucketName);

    /**
     * 获取路径下文件列表
     *
     * @param bucketName
     * @param prefix     文件名称
     * @param recursive  是否递归查找，false：模拟文件夹结构查找
     *
     * @return 二进制流
     */
    public Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive);


    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName
     *
     * @return
     */
    List<String> listObjectNames(String bucketName);

    /**
     * 上传文件，无需指定文件MIME类型
     * @param inputStream
     * @param bucketName
     * @param objectName
     * @return
     */
    public String putObject(InputStream inputStream, String bucketName, String objectName);

    /**
     *
     * @param bucketName
     * @param inputStream
     * @param originalFileName
     * @return
     */
    public String putObject(String bucketName, InputStream inputStream, String originalFileName);

        /**
         * 上传文件，返回文件外链地址
         *
         * @param inputStream
         * @param bucketName
         * @param objectName
         * @param fileType
         *
         * @return
         */
    public String putObject(InputStream inputStream, String bucketName, String objectName, String fileType) throws Exception;



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
    public UploadResponse uploadVideo(InputStream inputStream, String bucketName, String originalFilename);


    /**
     * 文件流下载
     *
     * @param bucketName
     * @param objectName
     *
     * @return
     */
    InputStream downloadObject(String bucketName, String objectName);

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
    public InputStream downloadObject(String bucketName, String objectName, long offset, long length);

    /**
     * 获取文件
     *
     * @param bucketName
     * @param objectName
     *
     * @return
     */
    public InputStream getObject(String bucketName, String objectName);


    /**
     * 获取文件信息, 如果抛出异常则说明文件不存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     *
     * @return
     */
    public String getFileStatusInfo(String bucketName, String objectName);

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectName
     */
    boolean removeObject(String bucketName, String objectName);


    /**
     * 批量删除文件
     *
     * @param bucketName
     * @param objectNameList
     *
     * @return
     */
    boolean removeListObject(String bucketName, List<String> objectNameList);

    /**
     * 获取文件路径
     *
     * @param bucketName
     * @param objectName
     *
     * @return
     */
    String getObjectUrl(String bucketName, String objectName);


    /**
     * 上传头像
     * @param request
     * @return
     */
    ResponseResult uploadAvatar(UploadAvatarRequest request);

    /**
     * 上传聊天图片
     * @param image
     * @param bucket
     * @param senderId
     * @return
     */
    String putChatImage(MultipartFile image, String bucket, Long senderId);

    /**
     * 上传笔记图片
     * @param image
     * @param bucket
     * @param noteId
     * @return
     */
    String putNoteImage(MultipartFile image, String bucket, Long noteId);


    /**
     * 上传聊天附件
     * @param file
     * @param bucket
     * @param senderId
     * @return
     */
    String putChatFile(MultipartFile file, String bucket, Long senderId);

    /**
     * 上传本地文件
     * @param mergeFilePath
     * @param bucket
     * @param hash
     * @return
     */
    String putLocalFile(String mergeFilePath, String bucket, String hash);

    /**
     * 上传笔记附件
     * @param file
     * @param noteId
     * @param bucket
     * @return
     */
    String putNoteAnnex(MultipartFile file, String bucket, Long noteId);
}