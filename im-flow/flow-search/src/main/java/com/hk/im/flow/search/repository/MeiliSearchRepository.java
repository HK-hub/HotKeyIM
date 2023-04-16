package com.hk.im.flow.search.repository;

import com.hk.im.domain.annotation.MeiliSearchIndex;
import com.hk.im.flow.search.operation.DocumentOperations;
import com.hk.im.flow.search.result.SearchResult;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author : HK意境
 * @ClassName : MeiliRepository
 * @date : 2023/4/16 13:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class MeiliSearchRepository<T> implements InitializingBean, DocumentOperations<T> {

    /**
     * 初始化的时候给默认值
     */
    private Index index;
    private Class<T> clazz;

    private static GsonJsonHandler jsonHandler = new GsonJsonHandler();

    @Resource
    private Client client;


    @Override
    public T get(String identifier) {
        T t = null;
        try {
            t = index.getDocument(identifier, clazz);
        } catch (Exception e) {
            log.info("get the document:{}, by identifier failed:{}", clazz.getName(), e);
        }
        return t;
    }

    @Override
    public List<T> list() {
        Results<T> tResults = new Results<>();
        try{
            tResults = this.index.getDocuments(clazz);
        }catch(Exception e){
            log.info("get the document:{}, by list failed:{}", clazz.getName(), e);
        }
        return Arrays.asList(tResults.getResults());
    }

    @Override
    public List<T> list(int limit) {

        Results<T> tResults = new Results<>();
        DocumentsQuery query = new DocumentsQuery();
        try {
            query.setOffset(0).setLimit(limit);
            tResults = this.index.getDocuments(query, clazz);
        } catch (MeilisearchException e) {
            log.info("get the document:{}, by list failed, parameters={}, cause={}", clazz.getName(), query, e);
        }

        return Arrays.asList(tResults.getResults());
    }

    @Override
    public List<T> list(int offset, int limit) {
        List<T> list = this.list(offset + limit);
        return list.subList(offset,  list.size());
    }

    @Override
    public T add(T document) {
        List<T> list = Collections.singletonList(document);
        long add = this.add(list);
        if (add > 0) {
            return document;
        }
        return null;
    }

    @Override
    public long update(T document) {
        List<T> list = Collections.singletonList(document);
        return update(list);
    }

    @Override
    public long add(List<T> documents) {
        TaskInfo taskInfo = null ;
        try {
            taskInfo = index.addDocuments(jsonHandler.encode(documents));
        } catch (Exception e) {
            log.info("add the document:{}, by list failed, parameters={}, cause={}", clazz.getName(), documents, e);
        }
        return taskInfo.getTaskUid();
    }

    @Override
    public long update(List<T> documents) {

        TaskInfo taskInfo = null ;
        try {
            taskInfo = index.updateDocuments(jsonHandler.encode(documents));
        } catch (Exception e) {
            log.info("update the document:{}, by list failed, parameters={}, cause={}", clazz.getName(), documents, e);
        }
        return taskInfo.getTaskUid();
    }

    @Override
    public long delete(String identifier) {
        TaskInfo taskInfo = null ;
        try {
            taskInfo = index.deleteDocument(identifier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return taskInfo.getTaskUid();
    }

    @Override
    public long deleteBatch(String... identifiers) {
        return this.deleteBatch(Arrays.asList(identifiers));
    }

    @Override
    public long deleteBatch(List<String> identifierList) {
        TaskInfo taskInfo = null ;
        try {
            taskInfo = index.deleteDocuments(identifierList);
        } catch (Exception e) {
            log.info("delete the document:{}, by list failed, parameters={}, cause={}", clazz.getName(), identifierList, e);
        }
        return taskInfo.getTaskUid();
    }

    @Override
    public long deleteAll() {
        TaskInfo taskInfo = null ;
        try {
            taskInfo = index.deleteAllDocuments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return taskInfo.getTaskUid();
    }

    @Override
    public List<T> query(String query) {
        SearchResult<T> tSearchResult = new SearchResult<>();
        try{
            String rawSearch = this.index.rawSearch(query);
            tSearchResult = jsonHandler.decode(rawSearch,SearchResult.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return tSearchResult.getHits();
    }

    @Override
    public List<T> query(SearchRequest searchRequest) {
        List<T> list =  new ArrayList<>();
        try {
            Searchable search = index.search(searchRequest);
            ArrayList<HashMap<String, Object>> hits = search.getHits();
            String encode = jsonHandler.encode(hits);
            list = jsonHandler.decode(encode, List.class);
        }catch (Exception e) {
            log.info("search the document:{}, by request failed, parameters={}, cause={}", clazz.getName(), searchRequest, e);

        }
        return list;
    }


    /**
     * 初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.initIndex();
    }


    /**
     * 初始化索引信息
     */
    private void initIndex() throws MeilisearchException {
        Class<? extends MeiliSearchRepository> aClass = getClass();
        clazz = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        MeiliSearchIndex annoIndex = clazz.getAnnotation(MeiliSearchIndex.class);
        // 索引名称
        String idx = annoIndex.index();
        String primaryKey = annoIndex.primaryKey();
        if (StringUtils.isEmpty(idx)) {
            idx = clazz.getSimpleName();
        }
        if (StringUtils.isEmpty(primaryKey)) {
            primaryKey = "id";
        }

        // 获取或创建索引
        Index index ;
        try {
            //如果不指定索引， 默认就使用表名称
            index = client.getIndex(idx);
        }catch (Exception e) {
            index = null;
        }
        if (Objects.isNull(index)) {
            TaskInfo taskInfo = client.createIndex(idx);
            index=  client.getIndex(idx);
        }
        this.index = index;
    }

}
