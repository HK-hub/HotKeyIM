package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.vo.UserVO;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : UserMapStructure
 * @date : 2022/12/31 12:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface UserMapStructure {

    public static final UserMapStructure INSTANCE = Mappers.getMapper(UserMapStructure.class);

    public UserVO toVo(User user, UserInfo userInfo);

    public User toUser(UserVO userVO);

    @Mapping(source = "userVO.username", target = "nickname")
    @Mapping(source = "userVO.id", target = "userId")
    public UserInfo toUserInfo(UserVO userVO);

}

