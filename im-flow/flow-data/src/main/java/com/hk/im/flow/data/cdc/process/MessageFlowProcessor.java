package com.hk.im.flow.data.cdc.process;

import com.alibaba.fastjson2.JSON;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.client.service.GroupMemberService;
import com.hk.im.client.service.GroupService;
import com.hk.im.client.service.UserService;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.entity.User;
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
public class MessageFlowProcessor implements RecordProcessor<MessageFlow>{

    @Resource(name = "meiliClient")
    private Client client;

    @Resource
    private ChatMessageService chatMessageService;

    private Index index;

    @Override
    public MessageFlow create(MessageFlow flow) {
        // 如果消息类型不为文本消息则不进行同步
        MessageConstants.ChatMessageType messageType = MessageConstants.ChatMessageType.values()[flow.getMessageType()];
        if (messageType != MessageConstants.ChatMessageType.TEXT) {
            return flow;
        }

        // 构建数据
        MessageDocument messageDocument = this.buildMessageDocument(flow);

        // 更新文档
        try {
            this.index.addDocuments(JSON.toJSONString(messageDocument));
        } catch (MeilisearchException e) {
            log.info("add document failed:{}", messageDocument, e);
        }

        return null;
    }

    @Override
    public MessageFlow update(MessageFlow before, MessageFlow after) {

        // 如果消息类型不为文本消息则不进行同步
        MessageConstants.ChatMessageType messageType = MessageConstants.ChatMessageType.values()[after.getMessageType()];
        if (messageType != MessageConstants.ChatMessageType.TEXT) {
            return after;
        }

        // 构建数据
        MessageDocument messageDocument = this.buildMessageDocument(after);

        // 更新文档
        try {
            this.index.updateDocuments(JSON.toJSONString(messageDocument));
        } catch (MeilisearchException e) {
            log.info("update document failed:{}", messageDocument, e);
        }

        return null;
    }

    @Override
    public MessageFlow remove(MessageFlow before, MessageFlow after) {

        // 如果消息类型不为文本消息则不进行同步
        MessageConstants.ChatMessageType messageType = MessageConstants.ChatMessageType.values()[after.getMessageType()];
        if (messageType != MessageConstants.ChatMessageType.TEXT) {
            return after;
        }

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
    private MessageDocument buildMessageDocument(MessageFlow after) {

        // 查询消息
        ChatMessage messageContent = this.chatMessageService.getById(after.getMessageId());
        // 查询发送者
        // User sender = this.userService.getById(after.getSenderId());

        MessageDocument document = new MessageDocument();
        document.setId(after.getId())
                .setContent(messageContent.getContent())
                .setCreateTime(after.getCreateTime())
                .setSenderId(after.getSenderId())
                .setReceiverId(after.getReceiverId());

        return document;
    }


    @PostConstruct
    public void init() {
        // 初始化 index
        try {
            this.index = client.getIndex("message");
            if (Objects.isNull(index)) {
                client.createIndex("message");
                this.index = this.client.getIndex("message");
            }
        } catch (MeilisearchException e) {
            log.info("get or create index failed: {}, {}", "message", e);
        }
    }

}
