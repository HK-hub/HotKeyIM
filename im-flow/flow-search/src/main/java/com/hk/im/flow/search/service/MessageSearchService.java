package com.hk.im.flow.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.flow.search.repository.ChatMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : MessageSearchService
 * @date : 2023/4/16 21:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MessageSearchService {

    @Resource
    private ChatMessageRepository chatMessageRepository;
    @Resource
    private ChatMessageService chatMessageService;

    /**
     * 全量同步消息
     * @return
     */
    public ResponseResult fullSyncMessage() {

        // 批量同步
        long total = this.chatMessageService.count();
        // 每次最多 1000 条消息
        long batch = total / 500;

        int actual = 0;
        for (long i = 1; i <= batch; i++) {
            Page<ChatMessage> page = this.chatMessageService.page(Page.of(i, 500, true));
            long count = this.chatMessageRepository.add(page.getRecords());
            actual += count;
        }

        return ResponseResult.SUCCESS(Map.of("total",total, "actual", actual));
    }



}
