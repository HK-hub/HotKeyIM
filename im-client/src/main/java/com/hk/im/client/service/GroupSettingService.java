package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.entity.GroupSetting;
import com.hk.im.domain.vo.GroupSettingVO;

/**
 *
 */
public interface GroupSettingService extends IService<GroupSetting> {


    /**
     * 获取群聊设置
     * @param groupId
     * @return
     */
    GroupSettingVO getGroupSetting(Long groupId);
}
