package com.hk.im.flow.search.query;

import com.hk.im.domain.annotation.FieldType;
import com.hk.im.domain.annotation.RedisSearch;
import com.hk.im.domain.annotation.RedisSearchField;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.flow.search.redis.FieldsEntity;
import com.hk.im.flow.search.redis.RedisSearchUtils;
import io.redisearch.Schema;
import io.redisearch.client.Client;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.exceptions.JedisDataException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LambdaRedisSearchUpdate<T> {


    private T t;
    Client client = null;

    public LambdaRedisSearchUpdate(T t) {
        this.t = t;
        String indexName = getIndexName();
        // client = new Client(indexName, "8.141.169.86", 56379, 500, 1);
        client = RedisSearchUtils.getClient(indexName);
        Map<String, Object> info = null;
        try {
            info = client.getInfo();
            System.out.println(info);
        } catch (JedisDataException e) {
            if (e.getMessage().equals("Unknown Index name")) {
                // 说明没有 这个 index 要创建
                if(!createindex()){
                    throw new RuntimeException("创建 索引失败");
                }
            }
        }
    }

    private boolean createindex() {
        Schema schema = new Schema();
        List<FieldsEntity> entityFields = getEntityFields();

        entityFields.forEach(ent -> {
            FieldType fieldType = ent.getFieldType();
            if(fieldType.equals(FieldType.STRING)){
                schema.addTextField(ent.getFieldName(),255);
            }else if(fieldType.equals(FieldType.NUMERIC)){
                schema.addNumericField(ent.getFieldName());
            }
        });
        return client.createIndex(schema, Client.IndexOptions.Default());
    }

    private List<FieldsEntity> getEntityFields() {

        List<FieldsEntity> FieldsEntitys = new ArrayList<>();

        // 拿到该类
        Class<?> clz = t.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            Type genericType = field.getGenericType();
            String name = field.getName();
            RedisSearchField annotation = field.getAnnotation(RedisSearchField.class);
            if(annotation == null){
                FieldType parameterType = FieldType.getParameterType(genericType.getTypeName());
                FieldsEntitys.add(new FieldsEntity(parameterType,name));
            }else{
                FieldType parameterType ;
                if(annotation.exist()){
                    String s = annotation.fieldName();
                    if(!StringUtils.isEmpty(s)){
                        name = s;
                    }
                    if(annotation.fieldType().equals(FieldType.AUTO)){
                        parameterType = FieldType.getParameterType(genericType.getTypeName());
                    }else{
                        parameterType = annotation.fieldType();
                    }
                    FieldsEntitys.add(new FieldsEntity(parameterType,name));
                }
            }

        }
        if (CollectionUtils.isEmpty(FieldsEntitys)){
            throw new RuntimeException(clz.getName()+"中没有有效属性, 请重新定义");
        }
        return FieldsEntitys;
    }

    private String getIndexName() {

        RedisSearch annotation = t.getClass().getAnnotation(RedisSearch.class);
        if (null != annotation) {
            return annotation.indexName();
        }
        throw new RuntimeException(t.getClass().getName() + "没找到 @RedisSearch 注解");

    }
    public boolean insert(String docId, T t) {
        Map<String, Object> documentByEntity = getDocumentByEntity(t);
        return client.addDocument(docId, documentByEntity);
    }
    private Map<String, Object> getDocumentByEntity(T t) {

        Map<String, Object> document = new HashMap<>();
        // 拿到该类
        Class<?> clz = t.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            String key = null;
            Object value = null;

            field.setAccessible(true);
            try {
                value = field.get(t);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            RedisSearchField annotation = field.getAnnotation(RedisSearchField.class);
            if(annotation == null ){
                key = field.getName();
                document.put(key,value);

            }else if (annotation.exist()){
                String s = annotation.fieldName();
                if(!StringUtils.isEmpty(s)){
                    key=s;
                }else{
                    key=field.getName();
                }
                document.put(key,value);
            }

        }

        if(CollectionUtils.isEmpty(document)){
            throw new RuntimeException(clz.getName()+"中没有有效属性, 请重新定义");
        }
        return document;
    }

    public boolean delete(String docId) {
        return client.deleteDocument(docId);
    }

    public boolean update(String docId, T t) {
        Map<String, Object> documentByEntity = getDocumentByEntity(t);
        return client.updateDocument(docId,1, documentByEntity);
    }


    public static void main(String[] args) {
        LambdaRedisSearchUpdate<ChatMessage> update = new LambdaRedisSearchUpdate<>(new ChatMessage());
    }
}