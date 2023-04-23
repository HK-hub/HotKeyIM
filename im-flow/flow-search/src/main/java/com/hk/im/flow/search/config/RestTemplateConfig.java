package com.hk.im.flow.search.config;

import com.feilong.net.http.builder.HttpClientBuilder;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {


    /**
     * 没有实例化RestTemplate时，初始化RestTemplate
     * @return
     */
    @ConditionalOnMissingBean(RestTemplate.class)
    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        return restTemplate;
    }

    /**
     * 使用OkHttpClient作为底层客户端
     * @return
     */
    private ClientHttpRequestFactory getClientHttpRequestFactory(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        return new OkHttp3ClientHttpRequestFactory(okHttpClient);
    }

}