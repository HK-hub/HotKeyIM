package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.PersonalTag;

import java.util.List;

/**
 *
 */
public interface PersonalTagService extends IService<PersonalTag> {

    ResponseResult createPersonalTag(PersonalTag personalTag);

    ResponseResult deleteUserPersonalTag(PersonalTag personalTag);

    List<PersonalTag> getUserPersonalTags(Long userId);

}
