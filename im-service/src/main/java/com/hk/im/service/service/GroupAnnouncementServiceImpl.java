package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.GroupAnnouncementService;
import com.hk.im.domain.entity.GroupAnnouncement;
import com.hk.im.domain.vo.GroupAnnouncementVO;
import com.hk.im.infrastructure.mapper.GroupAnnouncementMapper;
import com.hk.im.infrastructure.mapstruct.GroupAnnouncementMapStructure;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName : GroupAnnouncementServiceImpl
 * @author : HK意境
 * @date : 2023/2/13 13:24
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class GroupAnnouncementServiceImpl extends ServiceImpl<GroupAnnouncementMapper, GroupAnnouncement>
    implements GroupAnnouncementService {

    /**
     * 获取群聊公告
     * @param groupId
     * @return
     */
    @Override
    public List<GroupAnnouncementVO> getGroupAnnouncementList(Long groupId) {

        List<GroupAnnouncement> announcementList = this.lambdaQuery()
                .eq(GroupAnnouncement::getGroupId, groupId)
                .list();
        if (CollectionUtils.isEmpty(announcementList)) {
            // 群聊公告不存在
            announcementList = Collections.emptyList();
        }

        // 转换为 VO
        List<GroupAnnouncementVO> announcementVOList = announcementList.stream()
                .map(GroupAnnouncementMapStructure.INSTANCE::toVO)
                .toList();

        return announcementVOList;
    }


    /**
     * 获取群聊最新公告
     * @param groupId
     * @return
     */
    @Override
    public GroupAnnouncement getGroupLatestAnnouncement(String groupId) {

        GroupAnnouncement latest = this.lambdaQuery()
                .eq(GroupAnnouncement::getGroupId, groupId)
                .orderByDesc(GroupAnnouncement::getUpdateTime)
                .one();
        return latest;
    }



}




