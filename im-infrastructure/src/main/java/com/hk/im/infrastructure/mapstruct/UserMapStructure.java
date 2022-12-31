package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.vo.UserVO;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import org.mapstruct.Mapper;
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


}

