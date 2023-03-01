package com.hk.im.server.common.message;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : HK意境
 * @ClassName : DataContainer
 * @date : 2023/2/28 18:59
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@ToString
@Accessors(chain = true)
public class DataContainer implements Serializable {

    // 动作类型
    protected Integer actionType;

    // 事件类型
    protected String event;

    // 用户聊天内容消息
    protected AbstractMessage message;

    // 扩展
    protected String extra;

}
