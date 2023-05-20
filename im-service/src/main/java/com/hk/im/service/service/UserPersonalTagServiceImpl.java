package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.UserPersonalTagService;
import com.hk.im.domain.entity.UserPersonalTag;
import com.hk.im.infrastructure.mapper.UserPersonalTagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : UserPersonalTagServiceImpl
 * @author : HK意境
 * @date : 2023/5/12 23:58
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class UserPersonalTagServiceImpl extends ServiceImpl<UserPersonalTagMapper, UserPersonalTag>
    implements UserPersonalTagService {

    /**
     * 获取用户标签映射关系
     * @param tagId
     * @param userId
     * @return
     */
    @Override
    public UserPersonalTag getUserPersonalTag(Long tagId, Long userId) {

        UserPersonalTag one = this.lambdaQuery()
                .eq(UserPersonalTag::getUserId, userId)
                .eq(UserPersonalTag::getTagId, tagId)
                .eq(UserPersonalTag::getDeleted, Boolean.FALSE)
                .one();

        return one;
    }

    /**
     * 移除用户标签映射关系
     * @param id
     * @return
     */
    @Override
    public Boolean removeUserTagById(Long id) {

        boolean remove = this.lambdaUpdate()
                .eq(UserPersonalTag::getId, id)
                .set(UserPersonalTag::getDeleted, Boolean.TRUE)
                .update();

        return remove;
    }

    /**
     * 获取用户个性标签映射关系集合
     * @param userId
     * @return
     */
    @Override
    public List<UserPersonalTag> getUserPersonalTags(Long userId) {

        List<UserPersonalTag> list = this.lambdaQuery()
                .eq(UserPersonalTag::getUserId, userId)
                .eq(UserPersonalTag::getDeleted, Boolean.FALSE)
                .list();

        return list;
    }


}




