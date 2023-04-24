package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.request.ModifyGroupInfoRequest;
import com.hk.im.domain.request.UserFindPolicyRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.hk.im.domain.entity.Group
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    // 修改群信息
    Integer updateGroupInfo(@Param("request") ModifyGroupInfoRequest request);

    // 通过关键字查询群聊
    List<Group> searchGroupByKeyword(@Param("keyword") String keyword, @Param("categoryList") List<Integer> categoryList);

    /**
     * 通过关键字在用户加入群聊中查群聊列表
     * @param groupIdList
     * @param keyword
     * @return
     */
    List<Group> selectGroupListByKeyword(@Param("groupIdList") List<Long> groupIdList, @Param("keyword") String keyword);

    /**
     * 查询公开群组
     * @param request
     * @return
     */
    List<Group> selectPublicGroupList(@Param("param") UserFindPolicyRequest request);
}




