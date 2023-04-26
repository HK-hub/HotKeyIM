package com.hk.im.admin.controller;

import com.hk.im.client.service.GroupAnnouncementService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.GroupAnnouncementRequest;
import com.hk.im.domain.vo.GroupAnnouncementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public ResponseResult publishAnnouncement(@RequestBody GroupAnnouncementRequest request) {

        return null;
    }


    /**
     * 获取群公告
     * @param groupId
     * @return
     */
    @GetMapping("/group/list")
    public ResponseResult getGroupAnnouncementList(@RequestParam("groupId") Long groupId) {

        List<GroupAnnouncementVO> announcementVOList = this.groupAnnouncementService.getGroupAnnouncementList(groupId);
        return ResponseResult.SUCCESS(announcementVOList);
    }

    /**
     * 编辑群公告
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public ResponseResult editAnnouncement(@RequestBody GroupAnnouncementRequest request) {

        return this.groupAnnouncementService.editGroupAnnouncement(request);
    }

}
