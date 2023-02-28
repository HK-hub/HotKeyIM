package com.hk.im.common.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : ResultEntity
 * @date : 2023/2/27 15:34
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@NoArgsConstructor
public class ResultEntity extends HashMap<String, Object> {

    public ResultEntity(String key, Object value) {
        this.put(key, value);
    }

    public ResultEntity(Map<String, Object> map) {
        this.putAll(map);
    }

    public ResultEntity add(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public static ResultEntity of(String k1, Object v1, String k2, Object v2) {
        ResultEntity entity = new ResultEntity(k1, v1);
        return entity.add(k2, v2);
    }

    public static ResultEntity of(String key, Object value) {
        return new ResultEntity(key, value);
    }

    public static ResultEntity of() {
        return new ResultEntity();
    }

    public static ResultEntity of(Map<String, Object> map) {
        return new ResultEntity(map);
    }

}
