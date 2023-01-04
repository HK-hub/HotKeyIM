package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : FriendMapStructure
 * @date : 2023/1/3 19:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface FriendMapStructure {

    public static FriendMapStructure INSTANCE = Mappers.getMapper(FriendMapStructure.class);

    @Mapping(source = "friend.id", target = "id")
    @Mapping(source = "userVO", target = "friend")
    @Mapping(source = "userVO.status", target = "status", defaultValue = "2")
    public FriendVO toVO(Friend friend, UserVO userVO);


    public List<FriendVO> toVOList(List<Friend> friendList);

}
