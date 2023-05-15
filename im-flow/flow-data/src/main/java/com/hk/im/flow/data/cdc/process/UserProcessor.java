package com.hk.im.flow.data.cdc.process;

import com.hk.im.domain.entity.User;

/**
 * @author : HK意境
 * @ClassName : UserProcessor
 * @date : 2023/5/15 18:58
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserProcessor implements RecordProcessor<User> {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User before, User after) {
        return null;
    }

    @Override
    public User remove(User before, User after) {
        return null;
    }
}
