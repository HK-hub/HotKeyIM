package com.hk.im.flow.search.util;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import io.redisearch.AggregationResult;
import io.redisearch.Query;
import io.redisearch.Schema;
import io.redisearch.SearchResult;
import io.redisearch.aggregation.AggregationBuilder;
import io.redisearch.aggregation.SortedField;
import io.redisearch.aggregation.reducers.Reducers;
import io.redisearch.client.Client;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedisSearchUtils {

    private Client client = null;


    /**
     * 配置redis搜索
     */
    private RedisSearchUtils() {
        if (client == null) {
            String indexName = "test";
            String host = "149.127.219.216";
            int port = 6379;
            String password = "";
            int timeout = 3000;
            int poolSize = 0;
            if (!StringUtils.isEmpty(password)) {
                // redis 设置了密码
                client = new Client(indexName, host, port, timeout, poolSize, password);
            }else {
                // redis 未设置密码
                client = new Client(indexName, host, port);
            }
        }
    }


    /**
     * 创建索引
     */
    public void createIndex(String title, String body, String price){
        Schema sc = new Schema()
                .addTextField(title, 5.0)
                .addTextField(body, 1.0)
                .addNumericField(price);
        client.createIndex(sc, Client.IndexOptions.defaultOptions());
    }

    /**
     * 将文档添加到索引
     * fields.put("title", "hello world");
     * fields.put("state", "NY");
     * fields.put("body", "lorem ipsum");
     * fields.put("price", 1337);
     * @param fields
     */
    public void addDocument(String docId, Map<String, Object> fields){
        client.addDocument(docId, fields);
    }

    /**
     * 创建复杂查询
     * @param queryString
     * @param price
     * @return
     */
    public SearchResult search(String queryString, String price){
        Query query = new Query(queryString)
                .addFilter(new Query.NumericFilter(price, 0, 1000))
                .limit(0,Integer.MAX_VALUE);
        return client.search(query);
    }


    /**
     * 聚合查询
     * @param query
     * @param price
     * @param state
     * @param avgprice
     * @param k
     * @return
     */
    public AggregationResult aggregate(String query, String price, String state, String avgprice, String k){
        AggregationBuilder builder = new AggregationBuilder(query)
                .apply("@".concat(price).concat("/1000"), k)
                .groupBy("@".concat(state), Reducers.avg("@".concat(k)).as(avgprice))
                .filter("@".concat(avgprice).concat(">=2"))
                .sortBy(Integer.MAX_VALUE, SortedField.asc("@".concat(state)));
        return client.aggregate(builder);
    }


    /**
     * 分词查询 先分词 在合并
     */
    public List<SearchResult> searchParticiple(String queryString, String price){
        List<SearchResult> result  = new ArrayList<>();
        //分词
        List<Term> termList = NLPTokenizer.segment(queryString);
        termList.stream().forEach(e->{
            Query query = new Query(e.word)
                    .addFilter(new Query.NumericFilter(price, 0, 1000))
                    .limit(0,Integer.MAX_VALUE);
            SearchResult search = client.search(query);
            result.add(search);
        });
        return result;
    }

    /**
     * 出推荐查询
     * @param queryString
     * @param price
     * @return
     */
    public List<SearchResult> searchSuggest(String queryString, String price){
        List<SearchResult> result  = new ArrayList<>();
        //推荐
        List<String> suggest = new Suggester().suggest(queryString, Integer.MAX_VALUE);
        suggest.stream().forEach(e->{
            Query query = new Query(e)
                    .addFilter(new Query.NumericFilter(price, 0, 1000))
                    .limit(0,Integer.MAX_VALUE);
            SearchResult search = client.search(query);
            result.add(search);
        });
        return result;
    }


}