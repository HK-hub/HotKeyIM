package com.hk.im.flow.data.cdc.process;

import com.alibaba.fastjson2.JSON;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.flow.search.document.FriendDocument;
import com.hk.im.flow.search.document.MessageDocument;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : MessageFlowProcessor
 * @date : 2023/5/15 19:01
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class FriendProcessor implements RecordProcessor<Friend>{

    @Resource(name = "meiliClient")
    private Client client;

    private Index index;

    @Override
    public Friend create(Friend friend) {

        // 构建数据
        FriendDocument friendDocument = this.buildFriendDocument(friend);

        // 更新文档
        try {
            this.index.addDocuments(JSON.toJSONString(friendDocument));
        } catch (MeilisearchException e) {
            log.info("add document failed:{}", friendDocument, e);
        }

        return null;
    }

    @Override
    public Friend update(Friend before, Friend after) {

        // 构建数据
        FriendDocument friendDocument = this.buildFriendDocument(after);

        // 更新文档
        try {
            this.index.updateDocuments(JSON.toJSONString(friendDocument));
        } catch (MeilisearchException e) {
            log.info("update document failed:{}", friendDocument, e);
        }

        return null;
    }

    @Override
    public Friend remove(Friend before, Friend after) {

        // 移除文档
        try {
            this.index.deleteDocument(String.valueOf(after.getId()));
        } catch (MeilisearchException e) {
            log.info("remove document failed:{}", before, e);
        }

        return null;
    }


    /**
     * 构建数据
     * @param after
     * @return
     */
    private FriendDocument buildFriendDocument(Friend after) {

        FriendDocument friendDocument = new FriendDocument();
        friendDocument.setId(after.getId())
                .setUserId(after.getUserId())
                .setFriendId(after.getUserId())
                .setNickname(after.getNickname())
                .setRemarkName(after.getRemarkName())
                .setRemarkInfo(after.getRemarkInfo());

        return friendDocument;
    }


    @PostConstruct
    public void init() {
        // 初始化 index
        try {
            this.index = client.getIndex("friend");
            if (Objects.isNull(index)) {
                client.createIndex("friend");
                this.index = this.client.getIndex("friend");
            }
        } catch (MeilisearchException e) {
            log.info("get or create index failed: {}, {}", "friend", e);
        }
    }

}
