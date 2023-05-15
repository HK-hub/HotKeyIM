package com.hk.im.flow.data.cdc.deserialize;

import com.ververica.cdc.connectors.shaded.org.apache.kafka.connect.json.JsonConverter;
import com.ververica.cdc.connectors.shaded.org.apache.kafka.connect.source.SourceRecord;
import com.ververica.cdc.connectors.shaded.org.apache.kafka.connect.storage.ConverterType;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : MyDebeziumDeserializationSchema
 * @date : 2023/5/15 17:04
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MyDebeziumDeserializationSchema implements DebeziumDeserializationSchema<String> {
    private static final long serialVersionUID = 1L;
    private transient JsonConverter jsonConverter;
    private final Boolean includeSchema;
    private Map<String, Object> customConverterConfigs;

    public MyDebeziumDeserializationSchema() {
        this(false);
    }

    public MyDebeziumDeserializationSchema(Boolean includeSchema) {
        this.includeSchema = includeSchema;
    }

    public MyDebeziumDeserializationSchema(Boolean includeSchema, Map<String, Object> customConverterConfigs) {
        this.includeSchema = includeSchema;
        this.customConverterConfigs = customConverterConfigs;
    }

    @Override
    public void deserialize(SourceRecord record, Collector<String> out) throws Exception {
        if (this.jsonConverter == null) {
            this.initializeJsonConverter();
        }

        byte[] bytes = this.jsonConverter.fromConnectData(record.topic(), record.valueSchema(), record.value());
        out.collect(new String(bytes));
    }

    private void initializeJsonConverter() {
        this.jsonConverter = new JsonConverter();
        HashMap<String, Object> configs = new HashMap(2);
        configs.put("converter.type", ConverterType.VALUE.getName());
        configs.put("schemas.enable", this.includeSchema);
        if (this.customConverterConfigs != null) {
            configs.putAll(this.customConverterConfigs);
        }

        this.jsonConverter.configure(configs);
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return BasicTypeInfo.STRING_TYPE_INFO;
    }
}
