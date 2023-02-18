package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.Group;
import com.hk.im.domain.vo.GroupDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : GroupDetailMapStructure
 * @date : 2023/2/18 16:15
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface GroupDetailMapStructure {

    public static GroupDetailMapStructure INSTANCE = Mappers.getMapper(GroupDetailMapStructure.class);

    public GroupDetailVO toVO(Group group);


}
