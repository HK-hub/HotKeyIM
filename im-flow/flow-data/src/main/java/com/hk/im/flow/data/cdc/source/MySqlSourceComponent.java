package com.hk.im.flow.data.cdc.source;


import com.hk.im.flow.data.cdc.sink.CustomSink;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : MySqlSourceComponent
 * @date : 2023/5/15 15:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MySqlSourceComponent implements ApplicationRunner {

    @Value("${flink.cdc.source.host}")
    private String host;

    private int port = 3306;

    @Value("${spring.datasource.druid.username}")
    private String username;

    @Value("${spring.datasource.druid.password}")
    private String password;

    @Resource
    private CustomSink customSink;

    private static final String[] TABLES = {"hk_im.tb_user", "hk_im.tb_user_info", "hk_im.tb_message_flow", "hk_im.tb_note",
                                            "hk_im.tb_friend", "hk_im.tb_group", "hk_im.tb_group_member"};


    @Override
    public void run(ApplicationArguments args) throws Exception {
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname(host)
                .port(3306)
                // 需要捕获数据的数据库
                .databaseList("hk_im")
                .tableList(TABLES)
                .username(username)
                .password(password)
                // 转换为JSON 字符串
                .deserializer(new JsonDebeziumDeserializationSchema())
                // 是否包含表结构的变化
                .includeSchemaChanges(true)
                /*
                 * initial初始化快照,即全量导入后增量导入(检测更新数据写入)
                 * latest:只进行增量导入(不读取历史变化)
                 * timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
                 */
                .startupOptions(StartupOptions.latest())
                .serverTimeZone("GMT+8")
                .build();

        // 启动一个 WebUI ，指定一个web-UI端口号
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8081);
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment(configuration);

        // 检查点间隔时间
        executionEnvironment.enableCheckpointing(5000);
        DataStreamSink<String> sink = executionEnvironment.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
                .addSink(this.customSink);

        executionEnvironment.execute();

    }
}
