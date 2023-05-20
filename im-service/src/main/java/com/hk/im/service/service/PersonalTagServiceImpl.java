package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.PersonalTagService;
import com.hk.im.client.service.UserPersonalTagService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.PersonalTag;
import com.hk.im.domain.entity.UserPersonalTag;
import com.hk.im.infrastructure.mapper.PersonalTagMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : PersonalTagServiceImpl
 * @author : HK意境
 * @date : 2023/5/20 11:28
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class PersonalTagServiceImpl extends ServiceImpl<PersonalTagMapper, PersonalTag>
    implements PersonalTagService {

    @Resource
    private UserPersonalTagService userPersonalTagService;

    /**
     * 创建个人标签
     * @param personalTag
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createPersonalTag(PersonalTag personalTag) {

        // 参数校验
        boolean preCheck = Objects.isNull(personalTag) || StringUtils.isEmpty(personalTag.getName());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL().setDataAsMessage("标签不能为空!");
        }

        // 创建标签
        boolean save = this.save(personalTag);
        if (BooleanUtils.isFalse(save)) {
            // 创建标签失败
            return ResponseResult.FAIL();
        }

        // 创建个人标签关系
        UserPersonalTag userPersonalTag = new UserPersonalTag();
        userPersonalTag.setTagId(personalTag.getId())
                .setUserId(UserContextHolder.get().getId())
                .setDeleted(Boolean.FALSE);
        // 保存映射关系
        save = this.userPersonalTagService.save(userPersonalTag);
        if (BooleanUtils.isFalse(save)) {
            // 保存失败
            return ResponseResult.FAIL();
        }

        return ResponseResult.SUCCESS(personalTag);
    }


    /**
     * 删除用户标签
     * @param personalTag
     * @return
     */
    @Override
    public ResponseResult deleteUserPersonalTag(PersonalTag personalTag) {

        // 参数校验
        boolean preCheck = Objects.isNull(personalTag) || Objects.isNull(personalTag.getId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL().setMessage("标签不存在!");
        }

        // 获取标签映射关系
        Long userId = UserContextHolder.get().getId();
        UserPersonalTag userPersonalTag = this.userPersonalTagService.getUserPersonalTag(personalTag.getId(), userId);

        // 校验标签是否本人
        if (Objects.isNull(userPersonalTag)) {
            // 该标签不存在
            return ResponseResult.FAIL().setMessage("标签不存在或已被删除!");
        }

        // 删除标签
        Boolean remove = this.userPersonalTagService.removeUserTagById(userPersonalTag.getId());
        if (BooleanUtils.isFalse(remove)) {
            // 删除失败
            return ResponseResult.FAIL().setMessage("移除标签失败!");
        }

        // 移除成功
        return ResponseResult.SUCCESS();
    }


    /**
     * 获取用户个性标签
     * @param userId
     * @return
     */
    @Override
    public List<PersonalTag> getUserPersonalTags(Long userId) {

        // 获取当前用户id
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }

        // 获取用户标签映射集合
        List<UserPersonalTag> userPersonalTags = this.userPersonalTagService.getUserPersonalTags(userId);

        // 获取标签id 集合
        List<Long> tagIdList = userPersonalTags.stream().map(UserPersonalTag::getTagId).collect(Collectors.toList());

        // 查询标签集合
        List<PersonalTag> personalTags = this.listByIds(tagIdList);

        // 响应数据
        return personalTags;
    }




}




