package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CollectEmoticonService;
import com.hk.im.client.service.EmoticonService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.EmoticonBO;
import com.hk.im.domain.entity.CollectEmoticon;
import com.hk.im.domain.entity.Emoticon;
import com.hk.im.infrastructure.mapstruct.EmoticonMapStructure;
import com.hk.im.infrastructure.mapper.EmoticonMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * @ClassName : EmoticonServiceImpl
 * @author : HK意境
 * @date : 2023/2/23 16:28
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class EmoticonServiceImpl extends ServiceImpl<EmoticonMapper, Emoticon>
    implements EmoticonService {

    @Resource
    private CollectEmoticonService collectEmoticonService;


    /**
     * 获取用户收藏表情包
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getUserEmoticonList(String userId) {

        // 获取用户收藏表情包列表
        List<CollectEmoticon> collectEmoticonList = this.collectEmoticonService.lambdaQuery()
                .eq(CollectEmoticon::getUserId, userId)
                .list();

        // 获取表情包
        List<EmoticonBO> emoticonBOList = collectEmoticonList.stream().map(collectEmoticon -> {
                    Emoticon emoticon = this.getById(collectEmoticon.getEmoticonId());
                    // 组装成BO
                    return EmoticonMapStructure.INSTANCE.toBO(emoticon, collectEmoticon);
                }).sorted(Comparator.comparing(EmoticonBO::getUpdateTime).reversed())
                .toList();

        // 响应数据
        return ResponseResult.SUCCESS(emoticonBOList);
    }
}




