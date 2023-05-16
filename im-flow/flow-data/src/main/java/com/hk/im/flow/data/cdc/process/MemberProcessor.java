package com.hk.im.flow.data.cdc.process;

import com.alibaba.fastjson2.JSON;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.flow.search.document.FriendDocument;
import com.hk.im.flow.search.document.MemberDocument;
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
public class MemberProcessor implements RecordProcessor<GroupMember>{

    @Resource(name = "meiliClient")
    private Client client;

    private Index index;

    @Override
    public GroupMember create(GroupMember member) {

        // 构建数据
        MemberDocument document = this.buildMemberDocument(member);

        // 更新文档
        try {
            this.index.addDocuments(JSON.toJSONString(document));
        } catch (MeilisearchException e) {
            log.info("add document failed:{}", document, e);
        }

        return null;
    }

    @Override
    public GroupMember update(GroupMember before, GroupMember after) {

        // 构建数据
        MemberDocument document = this.buildMemberDocument(after);

        // 更新文档
        try {
            this.index.updateDocuments(JSON.toJSONString(document));
        } catch (MeilisearchException e) {
            log.info("update document failed:{}", document, e);
        }

        return null;
    }

    @Override
    public GroupMember remove(GroupMember before, GroupMember after) {

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
    private MemberDocument buildMemberDocument(GroupMember after) {

        MemberDocument document = new MemberDocument();
        document.setId(after.getId())
                .setGroupId(after.getGroupId())
                .setGroupAccount(after.getGroupAccount())
                .setMemberId(after.getMemberId())
                .setStatus(after.getStatus())
                .setCreateTime(after.getCreateTime())
                .setMemberUsername(after.getMemberUsername())
                .setMemberRemarkName(after.getMemberRemarkName());

        return document;
    }


    @PostConstruct
    public void init() {
        // 初始化 index
        try {
            this.index = client.getIndex("member");
            if (Objects.isNull(index)) {
                client.createIndex("member");
                this.index = this.client.getIndex("member");
            }
        } catch (MeilisearchException e) {
            log.info("get or create index failed: {}, {}", "member", e);
        }
    }

}
