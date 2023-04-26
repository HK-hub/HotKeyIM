package com.hk.im.domain.request;

import com.hk.im.domain.entity.GroupMember;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : RemoveGroupMemberRequest
 * @date : 2023/1/20 21:42
 * @description : 踢出群聊
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class RemoveGroupMemberRequest {

    // 群id
    private Long groupId;

    // 被操作用户id
    private List<Long> memberIdList;

    // 操作执行者：群主， 管理员
    private Long operatorId;

    private List<GroupMember> memberList;


}
