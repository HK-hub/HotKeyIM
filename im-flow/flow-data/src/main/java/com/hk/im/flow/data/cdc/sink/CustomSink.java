package com.hk.im.flow.data.cdc.sink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

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
public class CustomSink extends RichSinkFunction<String> {

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
        System.out.println(">>>" + json);
    }
}
