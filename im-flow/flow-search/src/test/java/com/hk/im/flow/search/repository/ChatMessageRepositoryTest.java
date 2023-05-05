package com.hk.im.flow.search.repository;

import com.alibaba.fastjson.JSON;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : HK意境
 * @ClassName : ChatMessageRepositoryTest
 * @date : 2023/4/16 20:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@SpringBootTest
class ChatMessageRepositoryTest {

    public static void main(String[] args) throws Exception {
        try {

            JSONObject object = new JSONObject().put("id", "3").put("title", "Life of Pi")
                    .put("genres", new JSONArray("[\"Adventure\",\"Drama\"]"));

            Client client = new Client(new Config("http://47.120.6.12:7700", null));

            System.out.println(client.health());

            Index index = client.index("movies");

            // If the index 'movies' does not exist, Meilisearch creates it when you first add the documents.
            String jsonString = object.toString();
            index.addDocuments(jsonString);

            Thread.sleep(1000);

            System.out.println(client.index("movies").search("pi"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}