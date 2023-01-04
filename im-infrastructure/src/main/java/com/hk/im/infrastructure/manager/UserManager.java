package com.hk.im.infrastructure.manager;

import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.mapper.UserInfoMapper;
import com.hk.im.infrastructure.mapper.UserMapper;
import com.hk.im.infrastructure.mapstruct.UserMapStructure;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : UserManager
 * @date : 2022/12/30 18:50
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class UserManager {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoMapper userInfoMapper;


    /**
     * 保存用户和用户信息
     * @param user
     * @param userInfo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUserAndInfo(User user, UserInfo userInfo) {

        // 返回受影响的行数：插入成功
        int us = userMapper.insert(user);
        // 插入失败
        if (us < 0) {
            return false;
        }

        userInfo.setUserId(user.getId());
        int ui = userInfoMapper.insert(userInfo);

        return us > 0 && ui > 0;
    }

    /**
     * 根据提供的账号，手机号，邮箱号查询用户
     * @param value 账号，手机号，邮箱号
     * @return
     */
    public User findUserByAccountOrPhoneOrEmail(String value) {
        User user = userMapper.getUserByAccountOrPhoneOrEmail(value);
        return user;
    }


    /**
     * 根据用户ID 查询用户信息
     * @param userId
     * @return
     */
    public UserVO findUserAndInfo(String userId) {

        User user = this.userMapper.selectById(userId);
        UserInfo userInfo = this.userInfoMapper.selectById(userId);

        return UserMapStructure.INSTANCE.toVo(user, userInfo);
    }


    /**
     * 批量查询用户信息
     * @param userIdList
     * @return
     */
    public List<UserVO> findUserAndInfoList(List<Long> userIdList) {

        List<User> userList = this.userMapper.selectBatchIds(userIdList);

        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyList();
        }

        List<UserInfo> userInfoList = this.userInfoMapper.selectBatchIds(userIdList);
        Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, value -> value));

        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = UserMapStructure.INSTANCE.toVo(user, userInfoMap.get(user.getId()));
            return userVO;
        }).toList();

        return userVOList;
    }


}
