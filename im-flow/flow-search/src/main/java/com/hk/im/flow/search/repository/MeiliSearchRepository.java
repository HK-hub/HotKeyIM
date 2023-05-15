package com.hk.im.flow.search.repository;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hk.im.domain.annotation.MeiliSearchIndex;
import com.hk.im.domain.request.SearchDocumentRequest;
import com.hk.im.flow.search.operation.DocumentOperations;
import com.hk.im.flow.search.result.SearchResult;
import com.meilisearch.sdk.*;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
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
    private String indexName;

    private static GsonJsonHandler jsonHandler = new GsonJsonHandler();

    @Resource
    private Config config;
    @Resource
    private RestTemplate restTemplate;

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
        // 构建url:
        String url = this.config.getHostUrl() + "/indexes/" + this.indexName + "/documents";
        try{
            ResponseEntity<T> responseEntity = this.restTemplate.postForEntity(url, documents, clazz);
        }catch(Exception e){
            log.info("add the document:{}, by list failed,url={}, parameters={}, cause={}", clazz.getName(), url, documents, e);
        }
        return documents.size();
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
            // 构建url:
            String url = this.config.getHostUrl() + "/indexes/" + this.indexName + "/search";
            SearchRequest sr = SearchRequest.builder().q(query).build();
            ResponseEntity<SearchResult> response = this.restTemplate.postForEntity(url, sr, SearchResult.class);
            tSearchResult = response.getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
        return tSearchResult.getHits();
    }

    @Override
    public List<T> query(SearchDocumentRequest searchRequest) {
        List<T> list =  new ArrayList<>();
        try {
            // 构建url:
            String url = this.config.getHostUrl() + "/indexes/" + this.indexName + "/search";
            // 创建Content-Type头为JSON
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();
            // 根据ContentType构建请求体
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(searchRequest));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)  // 设置请求体
                    .build();

            Response response = client.newCall(request).execute();
            String string = response.body().string();
            JSONObject jsonObject = JSON.parseObject(string);

            // 转化为目标对象
            JSONArray hitsArray = jsonObject.getJSONArray("hits");
            for (Object o : hitsArray) {
                T object = JSON.parseObject(JSON.toJSONString(o), clazz);
                list.add(object);
            }
        }catch (Exception e) {
            log.info("search the document:{}, by request failed, parameters={}, cause=", clazz.getName(), searchRequest, e);
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
        clazz = (Class<T>) ((ParameterizedType) aClass.getGenericSuperclass()).getActualTypeArguments()[0];
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
        Index index = null;
        indexName = idx;

        log.info("init meili search entity index:{}", idx);
        /*try {
            //如果不指定索引， 默认就使用表名称
            index = client.getIndex(idx);
        }catch (Exception e) {
            log.info("client get index error:",e);
            index = null;
        }
        if (Objects.isNull(index)) {
            TaskInfo taskInfo = client.createIndex(idx);
            index=  client.getIndex(idx);
        }*/
        this.index = index;
    }

}
