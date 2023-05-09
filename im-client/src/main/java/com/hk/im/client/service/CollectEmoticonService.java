package com.hk.im.client.service;

import com.hk.im.domain.entity.CollectEmoticon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface CollectEmoticonService extends IService<CollectEmoticon> {

    public CollectEmoticon getUserCollectEmoticon(Long userId, Long emoticonId);


    /**
     * 移除用户收藏表情
     * @param collectEmoticon
     * @return
     */
    boolean removeUserCollectEmoticon(CollectEmoticon collectEmoticon);



}
