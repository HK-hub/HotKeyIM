package com.hk.im.infrastructure.manager;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.infrastructure.mapper.GroupMemberMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : GroupMemberManager
 * @date : 2023/1/22 20:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class GroupMemberManager {

    @Resource
    private GroupMemberMapper groupMemberMapper;


    /**
     * 修改群员群昵称
     * @param groupId
     * @param memberId
     * @param remarkName
     * @return
     */
    public GroupMember updateGroupMemberRemarkName(String groupId, String memberId, String remarkName) {
        return null;
    }


}
