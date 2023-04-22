package com.hk.im.flow.search.domain.dto;

import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.domain.vo.MessageVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : ContentIndexSearchResultDomain
 * @date : 2023/4/21 23:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class ContentIndexSearchResultDomain {


    /**
     * 好友列表
     */
    private List<FriendVO> friendVOList = new ArrayList<>();

    /**
     * 聊天消息列表
     */
    private List<MessageVO> messageVOList = new ArrayList<>();


    /**
     * 群聊列表
     */
    private List<GroupVO> groupVOList = new ArrayList<>();



}
