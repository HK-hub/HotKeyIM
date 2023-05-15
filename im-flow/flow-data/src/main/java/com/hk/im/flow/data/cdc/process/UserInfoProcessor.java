package com.hk.im.flow.data.cdc.process;

import com.hk.im.domain.entity.UserInfo;

/**
 * @author : HK意境
 * @ClassName : UserInfoProcessor
 * @date : 2023/5/15 18:59
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserInfoProcessor implements RecordProcessor<UserInfo> {
    @Override
    public UserInfo create(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo update(UserInfo before, UserInfo after) {
        return null;
    }

    @Override
    public UserInfo remove(UserInfo before, UserInfo after) {
        return null;
    }
}
