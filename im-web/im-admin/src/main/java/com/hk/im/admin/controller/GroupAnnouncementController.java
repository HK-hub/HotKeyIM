package com.hk.im.admin.controller;

import com.hk.im.client.service.GroupAnnouncementService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.GroupAnnouncementRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : GroupAnnouncementController
 * @date : 2023/1/23 18:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/announcement")
public class GroupAnnouncementController {

    @Resource
    private GroupAnnouncementService groupAnnouncementService;


    /**
     * 发布群公告
     * @param request
     * @return
     */
    @PostMapping("/publish")
    public ResponseResult publishAnnouncement(GroupAnnouncementRequest request) {
        return null;
    }



}
