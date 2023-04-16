package com.hk.im.flow.search.operation;

import com.hk.im.flow.search.result.SearchResult;
import com.meilisearch.sdk.SearchRequest;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : DocumentOperations
 * @date : 2023/4/16 14:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface DocumentOperations<T> {

    public T get(String identifier);

    public List<T> list();

    public List<T> list(int limit);

    public List<T> list(int offset, int limit);

    public T add(T document);

    public long update(T document);

    public long add(List<T> documents);

    public long update(List<T> documents);

    public long delete(String identifier);

    public long deleteBatch(String... identifiers);

    public long deleteBatch(List<String> identifierList);

    public long deleteAll();

    public List<T> query(String query);

    public List<T> query(SearchRequest searchRequest);


}
