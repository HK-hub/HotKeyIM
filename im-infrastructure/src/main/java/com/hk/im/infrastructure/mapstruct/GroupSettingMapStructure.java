package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.GroupSetting;
import com.hk.im.domain.vo.GroupSettingVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : GroupSettingMapStructure
 * @date : 2023/2/12 20:30
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface GroupSettingMapStructure {

    public static final GroupSettingMapStructure INSTANCE = Mappers.getMapper(GroupSettingMapStructure.class);

    public GroupSettingVO toVO(GroupSetting groupSetting);


}
