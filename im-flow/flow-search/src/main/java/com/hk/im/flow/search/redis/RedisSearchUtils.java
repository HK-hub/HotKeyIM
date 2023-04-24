package com.hk.im.flow.search.redis;

import io.redisearch.client.Client;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据 spring boot 的redis 的配置 创建一个 redis Search 的 Client
 * 并根据 indexName 放在本地 缓存中
 */
@Component
public class RedisSearchUtils {

    private static String host = "149.127.219.216";
    private static String password = null;
    private static int port = 6379;
    private static Duration timeout = Duration.ofSeconds(500L);
    private static int poolSize = 5;
    private static Map<String,Client> clients = new HashMap<>(16);

    @Value("${redis.search.host}")
    public void setHost(String host) {
        if(null == host){
            host = "localhost";
        }
        RedisSearchUtils.host = host;
    }
    @Value("${redis.search.password}")
    public void setPassword(String password) {

        if(StringUtils.isEmpty(password)){
            password=null;
        }
        RedisSearchUtils.password = password;
    }
    @Value("${spring.redis.port:6379}")
    public void setPort(Integer port) {

        RedisSearchUtils.port = port;
    }
    @Value("${spring.redis.timeout:500}")
    public void setTimeout(Duration timeout) {
        RedisSearchUtils.timeout = timeout;
    }
    @Value("${spring.redis.poolSize:5}")
    public void setPoolSize(Integer poolSize) {
        RedisSearchUtils.poolSize = poolSize;
    }



    public static Client getClient(String indexName) {

        if(clients.containsKey(indexName)){

            Client client = clients.get(indexName);

            try {
                Map<String, Object> info = client.getInfo();
                System.out.println(info);
            }catch (Exception e){
                e.printStackTrace();
                Client client1 = createClient(indexName);
                clients.put(indexName,client1);
                return client1;
            }

            return client;
        }else {
            Client client1 = createClient(indexName);
            clients.put(indexName,client1);
            return client1;
        }
    }

    private static Client createClient(String indexName) {
        int timeoutl = (int)timeout.getSeconds();
        if (StringUtils.isNotEmpty(password)) {
            return new Client(indexName, host, port, timeoutl, poolSize, password);
        }
        return new Client(indexName, host, port, timeoutl, poolSize);
    }

}