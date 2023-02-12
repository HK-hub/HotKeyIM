package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.vo.GroupMemberVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : GroupMemberMapStructure
 * @date : 2023/2/12 20:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface GroupMemberMapStructure {

    public static final GroupMemberMapStructure INSTANCE = Mappers.getMapper(GroupMemberMapStructure.class);

    public GroupMemberVO toVO(GroupMember groupMember);


}
