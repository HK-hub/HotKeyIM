package com.hk.im.flow.search.service;

import com.hk.im.client.service.FriendService;
import com.hk.im.client.service.GroupService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.flow.search.domain.dto.ContentIndexSearchResultDomain;
import com.hk.im.flow.search.domain.param.SearchChatContentDomain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : ContentIndexSearchService
 * @date : 2023/4/21 23:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class ContentIndexSearchService {

    @Resource
    private FriendService friendService;
    @Resource
    private GroupService groupService;

    /**
     * 查询聊天，好友，群组等内容索引
     * @param request
     * @return
     */
    public ResponseResult searchTalkOrFriendOrGroup(SearchChatContentDomain request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getKeyword());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.SUCCESS(new ContentIndexSearchResultDomain());
        }

        // 素材
        Long userId = request.getUserId();
        String keyword = request.getKeyword();
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }

        // 查询好友列表
        List<FriendVO> friendVOList = friendService.getUserFriendListByKeyword(userId, keyword);

        // 查询群聊列表
        List<GroupVO> groupVOList = this.groupService.getUserGroupListByKeyword(userId, keyword);

        // 查询聊天记录列表
        

        return null;
    }
}
