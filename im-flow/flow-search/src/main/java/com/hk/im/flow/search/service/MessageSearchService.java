package com.hk.im.flow.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.client.service.MessageFlowService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.request.IncrementalSyncRequest;
import com.hk.im.domain.request.SearchDocumentRequest;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.flow.search.query.LambdaRedisSearchQuery;
import com.hk.im.flow.search.query.LambdaRedisSearchUpdate;
import com.hk.im.flow.search.repository.ChatMessageRepository;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.MatchingStrategy;
import io.redisearch.Document;
import io.redisearch.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
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
    @Resource
    private MessageFlowService messageFlowService;

    /**
     * 全量同步消息
     * @return
     */
    public ResponseResult fullSyncMessage() {

        // 批量同步
        long total = this.messageFlowService.count();
        // 每次最多 1000 条消息
        long batch = total / 500;

        LambdaRedisSearchUpdate<MessageVO> searchUpdate = new LambdaRedisSearchUpdate<>(new MessageVO());
        int actual = 0;
        for (long i = 0; i <= batch; i++) {
            // 查询消息流水
            Page<MessageFlow> page = this.messageFlowService.page(Page.of(i, 500, true));

            // 查询消息
            List<MessageVO> messageVOList = page.getRecords().stream().map(flow -> {
                        // 封装为 messageVO
                        return this.messageFlowService.convertToMessageVO(flow);
                    }).sorted(Comparator.comparing(MessageVO::getCreateTime).reversed()
                            .thenComparingLong(MessageVO::getSequence).reversed())
                    .toList();
            // 保存到 RedisSearch
            for (MessageVO messageVO : messageVOList) {
                boolean insert = searchUpdate.insert(String.valueOf(messageVO.getId()), messageVO);
                if (BooleanUtils.isTrue(insert)) {
                    actual += 1;
                }
            }
        }

        return ResponseResult.SUCCESS(Map.of("total",total, "actual", actual));
    }


    /**
     * 查询chatMessage
     * @param request
     * @return
     */
    public ResponseResult searchChatMessage(SearchDocumentRequest request) {

        log.info("Searching chat message:request={}", request);

        LambdaRedisSearchQuery<MessageVO> searchQuery = new LambdaRedisSearchQuery<>(new MessageVO());
        SearchResult result = searchQuery.like(MessageVO::getContent, request.getQ())
                .offset(request.getOffset())
                .limit(request.getLimit())
                .search();
        log.info("Searching chat message:result={}", result);

        // 解析查询结果
        return ResponseResult.SUCCESS(result);
    }


    /**
     * 增量同步消息
     * @param request
     * @return
     */
    public ResponseResult incrementalSynchronizeChatMessage(IncrementalSyncRequest request) {
        return null;
    }
}
