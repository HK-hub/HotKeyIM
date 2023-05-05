package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.GroupAnnouncementService;
import com.hk.im.client.service.GroupMemberService;
import com.hk.im.client.service.UserService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.GroupAnnouncement;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.GroupAnnouncementRequest;
import com.hk.im.domain.vo.GroupAnnouncementVO;
import com.hk.im.infrastructure.mapper.GroupAnnouncementMapper;
import com.hk.im.infrastructure.mapstruct.GroupAnnouncementMapStructure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Resource
    private GroupMemberService groupMemberService;

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
                .map(notice -> {
                    // 查询作者
                    GroupMember theGroupMember = this.groupMemberService.getTheGroupMember(groupId, notice.getAuthor());
                    GroupAnnouncementVO announcementVO = GroupAnnouncementMapStructure.INSTANCE.toVO(notice);
                    announcementVO.setAuthorName(theGroupMember.getMemberUsername())
                            .setAvatar(theGroupMember.getMemberAvatar());
                    return announcementVO;
                })
                .collect(Collectors.toList());

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
                .last(" limit 1")
                .one();
        return latest;
    }

    /**
     * 获取群聊置顶公告
     * @param groupId
     * @return
     */
    @Override
    public GroupAnnouncement getGroupTopAnnouncement(Long groupId) {
        return null;
    }


    /**
     * 编辑群聊公告
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult editGroupAnnouncement(GroupAnnouncementRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getAnnouncementId()) || StringUtils.isEmpty(request.getTitle());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL();
        }

        Long groupId = request.getGroupId();
        Long announcementId = request.getAnnouncementId();
        Long userId = UserContextHolder.get().getId();

        // 查询公告
        Boolean res = false;
        GroupAnnouncement groupAnnouncement = this.getById(announcementId);
        if (Objects.isNull(groupAnnouncement)) {
            // 公告不存在，保存公告
            groupAnnouncement = new GroupAnnouncement();
            groupAnnouncement.setGroupId(groupId).setAuthor(userId)
                    .setTitle(request.getTitle()).setContent(request.getContent())
                    .setTop(request.getTop());
            res = this.save(groupAnnouncement);
        } else {
            // 公告已经存在进行修改：
            // 1.获取修改用户角色
            Boolean hasPermission = this.groupMemberService.checkMemberHasManagePermission(groupId, userId);
            if (BooleanUtils.isFalse(hasPermission)) {
                // 无权编辑修改
                return ResponseResult.FAIL().setMessage("抱歉你无权编辑公告!");
            }

            // 更新公告
            groupAnnouncement.setTitle(request.getTitle()).setContent(request.getContent())
                    .setTop(request.getTop()).setUpdateTime(LocalDateTime.now());
            res = this.updateById(groupAnnouncement);
        }

        if (BooleanUtils.isFalse(res)) {
            // 更新或保存公告失败
            return ResponseResult.FAIL().setMessage("编辑群公告失败!");
        }

        return ResponseResult.SUCCESS();
    }


}




