package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Entity com.hk.im.domain.entity.UserInfo
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {


    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @Select("select * from tb_user_info where user_id = #{userId}")
    public UserInfo getUserInfoByUserId(@Param("userId") Long userId);


}




