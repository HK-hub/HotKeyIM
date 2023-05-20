package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.entity.UserPersonalTag;

import java.util.List;

/**
 *
 */
public interface UserPersonalTagService extends IService<UserPersonalTag> {


    UserPersonalTag getUserPersonalTag(Long id, Long userId);

    Boolean removeUserTagById(Long id);

    public List<UserPersonalTag> getUserPersonalTags(Long userId);

}
