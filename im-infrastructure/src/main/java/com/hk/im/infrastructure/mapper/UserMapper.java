package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.hk.im.domain.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    public User getUserByAccountOrPhoneOrEmail(@Param("account") String account);


}




