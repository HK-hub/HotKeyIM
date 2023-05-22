package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.FriendService;
import com.hk.im.client.service.UserZoneService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.UserZone;
import com.hk.im.domain.request.zone.GetObservableNotesRequest;
import com.hk.im.infrastructure.mapper.UserZoneMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : UserZoneServiceImpl
 * @author : HK意境
 * @date : 2023/5/22 11:26
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class UserZoneServiceImpl extends ServiceImpl<UserZoneMapper, UserZone>
    implements UserZoneService {

    @Resource
    private  UserZoneMapper userZoneMapper;
    @Resource
    private FriendService friendService;

    /**
     * 获取发布的笔记说说
     * @param userId
     * @param noteId
     * @return
     */
    @Override
    public UserZone getPublishedNote(Long userId, Long noteId) {

        UserZone zone = this.userZoneMapper.selectPublishedNote(userId, noteId);
        return zone;
    }


    /**
     * 获取用户发布的笔记列表
     * @param userId
     * @return
     */
    @Override
    public List<UserZone> getUserPublishedNotes(Long userId) {

        List<UserZone> noteList = this.userZoneMapper.selectUserPublishedNoteList(userId);

        return noteList;
    }

    /**
     * 获取用户可以查看的说说列表
     * @param request
     * @return
     */
    @Override
    public ResponseResult getObservableNotes(GetObservableNotesRequest request) {

        // 参数校验
        if (Objects.isNull(request)) {
            // 校验失败
            return ResponseResult.FAIL();
        }
        Long userId = request.getUserId();
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }
        // 能够查看的说说：1.公开说说，2.指定某人可查看
        // 查询好友列表
        List<Friend> friendList = this.friendService.getFriendListByUserId(userId);
        List<Long> friendIdList = friendList.stream().map(Friend::getUserId).collect(Collectors.toList());
        // 加入自己
        friendIdList.add(userId);

        // 设置分页信息
        request.setCurrentPage((request.getCurrentPage() - 1) * request.getLimit());

        // 根据时间进行倒叙排序
        List<UserZone> userZoneList = this.userZoneMapper.selectObservableZoneNoteList(request, friendIdList);

        // 查询
        return null;
    }



}




