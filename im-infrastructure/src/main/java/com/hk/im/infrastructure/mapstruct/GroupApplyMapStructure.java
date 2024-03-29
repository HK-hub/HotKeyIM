package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.GroupApply;
import com.hk.im.domain.vo.GroupApplyVO;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.domain.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : GroupApplyMapStructure
 * @date : 2023/2/16 21:21
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface GroupApplyMapStructure {

    public static GroupApplyMapStructure INSTANCE = Mappers.getMapper(GroupApplyMapStructure.class);


    @Mapping(source = "groupApply.id", target = "id")
    @Mapping(source = "groupApply.status", target = "status")
    @Mapping(source = "groupApply.createTime", target = "createTime")
    @Mapping(source = "userVO", target = "senderVO")
    @Mapping(source = "groupApply.groupId", target = "groupId")
    public GroupApplyVO toVO(GroupApply groupApply, GroupVO groupVO, UserVO userVO);


}
