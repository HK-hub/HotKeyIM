package com.hk.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.FriendGroup;
import com.hk.im.service.service.FriendGroupService;
import com.hk.im.infrastructure.mapper.FriendGroupMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : FriendGroupServiceImpl
 * @author : HK意境
 * @date : 2023/2/8 21:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class FriendGroupServiceImpl extends ServiceImpl<FriendGroupMapper, FriendGroup> implements FriendGroupService{

    @Resource
    private FriendGroupMapper friendGroupMapper;

    /**
     * 获取用户默认分组
     * @param acceptorId
     * @return
     */
    @Override
    public ResponseResult getUserDefaultGroup(Long acceptorId) {
        FriendGroup defaultGroup = this.lambdaQuery().eq(FriendGroup::getUserId, acceptorId).one();
        if (Objects.isNull(defaultGroup)) {
            // 默认分组不存在，创建一个
            defaultGroup = new FriendGroup().setUserId(acceptorId).setName("默认分组");
            this.save(defaultGroup);
        }
        return ResponseResult.SUCCESS(defaultGroup);
    }

    @Override
    public ResponseResult getUserAllGroup(Long userId) {
        List<FriendGroup> list = this.lambdaQuery().eq(FriendGroup::getUserId, userId).list();
        if (CollectionUtils.isEmpty(list)) {
            // 分组不存在，创建默认分组集合
            FriendGroup defaultGroup = new FriendGroup().setUserId(userId).setName("默认分组");
            FriendGroup blackListGroup = new FriendGroup().setUserId(userId).setName("黑名单");
            FriendGroup carefulGroup = new FriendGroup().setUserId(userId).setName("特别关心");
            // 创建
            friendGroupMapper.insert(defaultGroup);
            friendGroupMapper.insert(blackListGroup);
            friendGroupMapper.insert(carefulGroup);
            // 设置
            list.add(defaultGroup);
            list.add(blackListGroup);
            list.add(carefulGroup);
        }
        return ResponseResult.SUCCESS(list);
    }
}




