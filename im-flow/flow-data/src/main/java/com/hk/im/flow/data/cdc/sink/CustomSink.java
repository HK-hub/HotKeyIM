package com.hk.im.flow.data.cdc.sink;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.flow.data.cdc.event.events.friend.FriendEvent;
import com.hk.im.flow.data.cdc.event.events.group.GroupEvent;
import com.hk.im.flow.data.cdc.event.events.group.MemberEvent;
import com.hk.im.flow.data.cdc.event.events.info.UserInfoEvent;
import com.hk.im.flow.data.cdc.event.events.message.MessageEvent;
import com.hk.im.flow.data.cdc.event.events.note.NoteEvent;
import com.hk.im.flow.data.cdc.event.events.user.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : CustomSink
 * @date : 2023/5/14 23:29
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class CustomSink extends RichSinkFunction<String> {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void open(Configuration parameters) throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void invoke(String json, Context context) throws Exception {

        // OP字段：增删改查，四个取值
        // 对于U操作，其数据部分同时包括了Before和After
        log.info(">>>:{}", json);

        // 序列化
        JSONObject jsonObject = JSON.parseObject(json);

        // 读操作忽略
        String op = jsonObject.getString("op");
        if (Objects.equals(op, "r")) {
            // 忽略
            return;
        }

        // 新增，修改，删除
        // 获取表名
        JSONObject source = jsonObject.getJSONObject("source");
        String table = source.getString("table");

        // 根据表名进行事件分发
        switch (table) {
            case "tb_user":
                this.applicationContext.publishEvent(new UserEvent(this, jsonObject));
                break;
            case "tb_user_info":
                this.applicationContext.publishEvent(new UserInfoEvent(this, jsonObject));
                break;
            case "tb_message_flow":
                this.applicationContext.publishEvent(new MessageEvent(this, jsonObject));
                break;
            case "tb_note":
                this.applicationContext.publishEvent(new NoteEvent(this, jsonObject));
                break;
            case "tb_friend":
                this.applicationContext.publishEvent(new FriendEvent(this, jsonObject));
                break;
            case "tb_group":
                this.applicationContext.publishEvent(new GroupEvent(this, jsonObject));
                break;
            case "tb_group_member":
                this.applicationContext.publishEvent(new MemberEvent(this, jsonObject));
                break;
            default:
                break;
        }


    }
}
