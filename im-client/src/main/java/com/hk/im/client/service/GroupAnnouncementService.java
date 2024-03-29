package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.GroupAnnouncement;
import com.hk.im.domain.request.GroupAnnouncementRequest;
import com.hk.im.domain.vo.GroupAnnouncementVO;

import java.util.List;

/**
 * @ClassName : GroupAnnouncementService
 * @author : HK意境
 * @date : 2023/2/13 13:23
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface GroupAnnouncementService extends IService<GroupAnnouncement> {

    /**
     * 获取群聊公告列表
     * @param groupId
     * @return
     */
    List<GroupAnnouncementVO> getGroupAnnouncementList(Long groupId);

    /**
     * 获取群聊最新公告
     * @param groupId
     * @return
     */
    GroupAnnouncement getGroupLatestAnnouncement(String groupId);

    /**
     * 获取群聊置顶公告
     * @param groupId
     * @return
     */
    GroupAnnouncement getGroupTopAnnouncement(Long groupId);

    /**
     * 编辑群聊公告
     * @param request
     * @return
     */
    ResponseResult editGroupAnnouncement(GroupAnnouncementRequest request);
}
