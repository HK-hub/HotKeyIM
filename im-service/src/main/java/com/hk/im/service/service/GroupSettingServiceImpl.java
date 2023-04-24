package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.GroupSettingService;
import com.hk.im.domain.entity.GroupSetting;
import com.hk.im.domain.vo.GroupSettingVO;
import com.hk.im.infrastructure.mapper.GroupSettingMapper;
import com.hk.im.infrastructure.mapstruct.GroupSettingMapStructure;
import org.springframework.stereotype.Service;

/**
 * @ClassName : GroupSettingServiceImpl
 * @author : HK意境
 * @date : 2023/2/13 13:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class GroupSettingServiceImpl extends ServiceImpl<GroupSettingMapper, GroupSetting> implements GroupSettingService {


    /**
     * 获取群聊设置
     * @param groupId
     * @return
     */
    @Override
    public GroupSettingVO getGroupSettingVO(Long groupId) {
        GroupSetting setting = this.getById(groupId);
        GroupSettingVO settingVO = GroupSettingMapStructure.INSTANCE.toVO(setting);
        return settingVO;
    }

    /**
     * 获取群聊设置
     * @param groupId
     * @return
     */
    @Override
    public GroupSetting getGroupSetting(Long groupId) {
        return this.getById(groupId);
    }
}




