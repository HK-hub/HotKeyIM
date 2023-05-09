package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.client.service.CollectEmoticonService;
import com.hk.im.client.service.EmoticonService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.EmoticonBO;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.CollectEmoticon;
import com.hk.im.domain.entity.Emoticon;
import com.hk.im.domain.request.emoticon.CollectUserEmoticonRequest;
import com.hk.im.infrastructure.mapstruct.EmoticonMapStructure;
import com.hk.im.infrastructure.mapper.EmoticonMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Resource
    private ChatMessageService chatMessageService;


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
                .collect(Collectors.toList());

        // 响应数据
        return ResponseResult.SUCCESS(emoticonBOList);
    }


    /**
     * 收藏用户表情包
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult collectUserEmoticon(CollectUserEmoticonRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getRecordId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 素材
        Long userId = request.getUserId();
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }
        Long recordId = request.getRecordId();

        // 查询消息记录
        ChatMessage record = this.chatMessageService.getById(recordId);
        if (Objects.isNull(record) || StringUtils.isEmpty(record.getUrl())) {
            // 消息记录不存在
            return ResponseResult.FAIL().setMessage("表情包不存在!");
        }

        // 保存表情
        Emoticon emoticon = new Emoticon();
        emoticon.setContent(request.getKeyword())
                .setType(2)
                .setUrl(record.getUrl());
        boolean save = this.save(emoticon);
        if (BooleanUtils.isFalse(save)) {
            // 保存表情失败
            return ResponseResult.FAIL();
        }

        // 保存用户表情包关系
        CollectEmoticon collectEmoticon = new CollectEmoticon()
                .setEmoticonId(emoticon.getId())
                .setUserId(userId);
        this.collectEmoticonService.save(collectEmoticon);

        // 响应成功
        return ResponseResult.SUCCESS();
    }


    /**
     * 移除用户收藏标签
     * @param emoticonId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult removeUserCollectEmoticon(Long emoticonId) {

        // 参数校验
        if (Objects.isNull(emoticonId)) {
            return ResponseResult.FAIL();
        }

        // 获取当前用户
        Long userId = UserContextHolder.get().getId();

        // 查询表情
        CollectEmoticon collectEmoticon = this.collectEmoticonService.getUserCollectEmoticon(userId, emoticonId);
        if (Objects.isNull(collectEmoticon)) {
            return ResponseResult.FAIL().setMessage("表情包不存在!");
        }

        // 删除收藏表情包关系
        boolean remove = this.collectEmoticonService.removeUserCollectEmoticon(collectEmoticon);
        if (BooleanUtils.isFalse(remove)) {
            // 移除关系失败
            return ResponseResult.FAIL();
        }
        // 移除表情包
        remove = this.removeById(emoticonId);
        if (BooleanUtils.isFalse(remove)) {
            // 移除表情包
            return ResponseResult.FAIL();
        }

        return ResponseResult.SUCCESS();
    }
}




