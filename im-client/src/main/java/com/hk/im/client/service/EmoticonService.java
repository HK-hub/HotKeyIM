package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Emoticon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName : EmoticonService
 * @author : HK意境
 * @date : 2023/2/23 16:28
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface EmoticonService extends IService<Emoticon> {


    /**
     * 获取用户收藏表情包
     * @param userId
     * @return
     */
    ResponseResult getUserEmoticonList(String userId);
}
