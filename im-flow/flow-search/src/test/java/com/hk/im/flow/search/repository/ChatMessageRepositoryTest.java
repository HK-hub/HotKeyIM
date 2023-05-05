package com.hk.im.flow.search.repository;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.SearchResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static String messageJSONString = " {\n" +
            "                \"id\":\"1654399175359488001\",\n" +
            "                \"messageId\":\"1654399175359488001\",\n" +
            "                \"messageFlowId\":\"1654399175380459522\",\n" +
            "                \"groupId\":null,\n" +
            "                \"senderId\":\"1611656761503215617\",\n" +
            "                \"senderMember\":{\n" +
            "                    \"talkType\":null,\n" +
            "                    \"userId\":\"1611656761503215617\",\n" +
            "                    \"groupId\":null,\n" +
            "                    \"avatar\":\"http://47.108.146.141:9000/user//user/avatar/public_avatar_mini_1611656761503215617.jpg?t=1678431334331\",\n" +
            "                    \"username\":\"西门大官人\",\n" +
            "                    \"remarkName\":null\n" +
            "                },\n" +
            "                \"receiverId\":\"1612030664754913281\",\n" +
            "                \"receiverMember\":{\n" +
            "                    \"talkType\":null,\n" +
            "                    \"userId\":\"1612030664754913281\",\n" +
            "                    \"groupId\":null,\n" +
            "                    \"avatar\":\"http://47.108.146.141:9000/user//user/avatar/public_avatar_mini_1612030664754913281.jpg\",\n" +
            "                    \"username\":\"令狐翅膀\",\n" +
            "                    \"remarkName\":\"令狐翅膀\"\n" +
            "                },\n" +
            "                \"chatType\":1,\n" +
            "                \"messageType\":1,\n" +
            "                \"sequence\":212,\n" +
            "                \"sendStatus\":2,\n" +
            "                \"signFlag\":1,\n" +
            "                \"deleted\":null,\n" +
            "                \"createTime\":\"2023-05-05 16:14:35\",\n" +
            "                \"updateTime\":\"2023-05-05 16:14:35\",\n" +
            "                \"messageFeature\":0,\n" +
            "                \"content\":\"消息json\",\n" +
            "                \"url\":null,\n" +
            "                \"extra\":null,\n" +
            "                \"layout\":\"right\",\n" +
            "                \"sender\":{\n" +
            "                    \"id\":1611656761503215600,\n" +
            "                    \"accessToken\":null,\n" +
            "                    \"expiresIn\":43200,\n" +
            "                    \"username\":\"西门大官人\",\n" +
            "                    \"account\":\"1673083889\",\n" +
            "                    \"phone\":null,\n" +
            "                    \"email\":\"3161880795@qq.com\",\n" +
            "                    \"bigAvatar\":\"http://47.108.146.141:9000/user//user/avatar/public_avatar_mini_1611656761503215617.jpg?t=1678431334331\",\n" +
            "                    \"miniAvatar\":\"http://47.108.146.141:9000/user//user/avatar/public_avatar_mini_1611656761503215617.jpg?t=1678431334331\",\n" +
            "                    \"qrcode\":\"http://47.108.146.141:9000/user//qrcode/public_qrcode_1673083889.jpg\",\n" +
            "                    \"qq\":null,\n" +
            "                    \"wechat\":null,\n" +
            "                    \"github\":null,\n" +
            "                    \"dingtalk\":null,\n" +
            "                    \"wallet\":0,\n" +
            "                    \"status\":null,\n" +
            "                    \"gender\":1,\n" +
            "                    \"age\":null,\n" +
            "                    \"birthday\":[\n" +
            "                        2022,\n" +
            "                        8,\n" +
            "                        17\n" +
            "                    ],\n" +
            "                    \"constellation\":null,\n" +
            "                    \"campus\":null,\n" +
            "                    \"major\":null,\n" +
            "                    \"job\":null,\n" +
            "                    \"city\":null,\n" +
            "                    \"interest\":null,\n" +
            "                    \"tag\":null,\n" +
            "                    \"signature\":\"我生来就是高山而非溪流，我欲于群峰之巅俯视平庸的沟壑；我生来就是人杰而非草芥，我站在伟人之肩藐视卑微的懦夫。\",\n" +
            "                    \"groupId\":null\n" +
            "                },\n" +
            "                \"receiver\":null,\n" +
            "                \"avatar\":\"http://47.108.146.141:9000/user//user/avatar/public_avatar_mini_1611656761503215617.jpg?t=1678431334331\",\n" +
            "                \"senderAvatar\":\"http://47.108.146.141:9000/user//user/avatar/public_avatar_mini_1611656761503215617.jpg?t=1678431334331\",\n" +
            "                \"receiverAvatar\":null,\n" +
            "                \"friendRemark\":\"西门大官人\",\n" +
            "                \"nickname\":\"西门大官人\",\n" +
            "                \"isCheck\":false\n" +
            "            }";

    public static void main(String[] args) throws Exception {
        try {

            JSONObject object = new JSONObject().put("id", "3").put("title", "Life of Pi")
                    .put("genres", new JSONArray("[\"Adventure\",\"Drama\"]"));

            Client client = new Client(new Config("http://47.120.6.12:7700", null));

            System.out.println(client.health());

            Index index = client.index("message");

            // If the index 'movies' does not exist, Meilisearch creates it when you first add the documents.
            String jsonString = object.toString();
            index.addDocuments(messageJSONString);

            Thread.sleep(1000);
            SearchResult result = client.index("message").search("消息json");
            ArrayList<HashMap<String, Object>> hits = result.getHits();
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}