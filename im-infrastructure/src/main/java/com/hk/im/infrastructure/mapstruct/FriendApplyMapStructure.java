package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.FriendApply;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.domain.vo.FriendApplyVO;
import com.hk.im.domain.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : FriendApplyMapStructure
 * @date : 2023/1/3 13:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface FriendApplyMapStructure {

    public static final FriendApplyMapStructure INSTANCE = Mappers.getMapper(FriendApplyMapStructure.class);

    @Mapping(source = "friendApply.id", target = "id")
    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "acceptor", target = "acceptor")
    @Mapping(source = "friendApply.status", target = "status")
    @Mapping(source = "friendApply.createTime", target = "createTime")
    public FriendApplyVO toVO(FriendApply friendApply, UserVO sender, UserVO acceptor);


}
