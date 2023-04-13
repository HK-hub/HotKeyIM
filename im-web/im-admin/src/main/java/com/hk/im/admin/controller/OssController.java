package com.hk.im.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hk.im.client.service.UserInfoService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.CloudFile;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.client.service.CloudFileService;
import com.hk.im.client.service.OssService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;


@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {
    @Resource
    private OssService ossService;
    @Resource
    private CloudFileService cloudFileService;
    @Resource
    private UserInfoService userInfoService;

    //判断上传的文件类型
    @PostMapping("upload/{userId}")
    public ResponseResult upload(MultipartFile file, @RequestParam String catalogue, @PathVariable Long userId) {

        // 获取用户存储信息
        UserInfo userInfo = this.userInfoService.getUserInfo(userId);

        long storage = userInfo.getStorage();
        long size = file.getSize();
        long result = storage + size;

        // 判断是否超出内存: 1GB
        if (result < 1073741824) {
            userInfo.setStorage(result);
            boolean b = this.userInfoService.updateById(userInfo);
            System.out.println(b);
            //获取文件名称
            String fileName = file.getOriginalFilename();
            //获取文件类型
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String type = fileType.substring(1);
            if (type.equals("mp3") || type.equals("mpeg") || type.equals("vma") || type.equals("aac") || type.equals("ra") || type.equals("am") || type.equals("rmx") || type.equals("mp3")
                    || type.equals("avi") || type.equals("mov") || type.equals("rmvb") || type.equals("rm") || type.equals("mp4") || type.equals("3gp") || type.equals("flv") || type.equals("ape") || type.equals("flac") || type.equals("wmv")) {
                //获取文件名称
                String title = fileName.substring(0, fileName.lastIndexOf("."));
                CloudFile file1 = new CloudFile();
                file1.setSize(size);
                if (type.equals("mp3") || type.equals("mpeg") || type.equals("vma") || type.equals("aac") || type.equals("ra") || type.equals("am") || type.equals("rmx")
                        || type.equals("ape") || type.equals("flac")) {
                    file1.setFiletype("audio");
                }
                if (type.equals("avi") || type.equals("mov") || type.equals("wmv") || type.equals("rmvb") || type.equals("rm") || type.equals("mp4") || type.equals("3gp") || type.equals("flv")) {
                    file1.setFiletype("video");
                }
                file1.setName(title);
                file1.setType(type);
                file1.setFDir(catalogue);
                String videoId = ossService.uploadfile(file);
                file1.setVideoId(videoId);

                return ResponseResult.SUCCESS(file1);
            } else {
                CloudFile file1 = ossService.upload(file, catalogue);

                if (file1.equals("")) {
                    return ResponseResult.ERROR();
                }
                return ResponseResult.SUCCESS(file1);
            }
        } else {
            return ResponseResult.FAIL("抱歉，您的存储空间超出限制了!");
        }

    }


}
