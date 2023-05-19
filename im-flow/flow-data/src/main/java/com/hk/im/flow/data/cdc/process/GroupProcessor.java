package com.hk.im.flow.data.cdc.process;

import com.alibaba.fastjson2.JSON;
import com.hk.im.domain.constant.GroupConstants;
import com.hk.im.domain.entity.Group;
import com.hk.im.flow.data.graph.entity.GroupNode;
import com.hk.im.flow.search.document.GroupDocument;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
public class GroupProcessor implements RecordProcessor<Group>{

    @Resource(name = "meiliClient")
    private Client client;

    // @Resource
    // private GroupRepository groupRepository;

    private Index index;

    private final String indexName = "group";

    @Override
    public Group create(Group group) {

        // 构建数据
        GroupDocument document = this.buildGroupDocument(group);

        // 更新文档
        try {
            Client client = new Client(new Config("http://47.120.6.12:7700/", null));
            Index index = client.index(indexName);
            index.addDocuments(JSON.toJSONString(document));
        } catch (MeilisearchException e) {
            log.info("add document failed:{}", document, e);
        }

        // 保存到Neo4j
        // GroupNode groupNode = this.buildGroupNode(group);
        try{
            // this.groupRepository.save(groupNode);
        }catch(Exception e){
            // log.info("add neo4j node failed:{}", groupNode, e);
        }

        return null;
    }

    @Override
    public Group update(Group before, Group after) {

        // 构建数据
        GroupDocument document = this.buildGroupDocument(after);

        // 更新文档
        try {
            Client client = new Client(new Config("http://47.120.6.12:7700/", null));
            Index index = client.index(indexName);
            index.updateDocuments(JSON.toJSONString(document));
        } catch (MeilisearchException e) {
            log.info("update document failed:{}", document, e);
        }

        // 更新节点
        // GroupNode groupNode = this.buildGroupNode(after);
        try{
            // this.groupRepository.save(groupNode);
        }catch(Exception e){
            // log.info("update node failed:{}", groupNode, e);
        }

        return null;
    }

    @Override
    public Group remove(Group before, Group after) {

        // 移除文档
        try {
            Client client = new Client(new Config("http://47.120.6.12:7700/", null));
            Index index = client.index(indexName);
            index.deleteDocument(String.valueOf(after.getId()));
        } catch (MeilisearchException e) {
            log.info("remove document failed:{}", before, e);
        }

        // 删除群聊节点
        // this.groupRepository.deleteById(after.getId());

        return null;
    }


    /**
     * 构建数据
     * @param group
     * @return
     */
    private GroupDocument buildGroupDocument(Group group) {

        GroupDocument document = new GroupDocument();
        document.setId(group.getId())
                .setGroupName(group.getGroupName())
                .setGroupAccount(group.getGroupAccount())
                .setDescription(group.getDescription());

        return document;
    }


    /**
     * 构建neo4j 节点数据
     * @param group
     * @return
     */
    private GroupNode buildGroupNode(Group group) {

        GroupNode groupNode = new GroupNode();
        groupNode.setId(group.getId())
                .setGroupAccount(groupNode.getGroupAccount())
                .setGroupType(GroupConstants.GroupCategory.values()[group.getGroupType()].getCategory())
                .setGroupName(group.getGroupName())
                .setDescription(group.getDescription());

        return groupNode;
    }

    @PostConstruct
    public void init() {
        // 初始化 index
        try {
            this.index = client.index(indexName);
        } catch (MeilisearchException e) {
            log.info("get or create index failed: {}, {}", indexName, e);
        }
    }

}
