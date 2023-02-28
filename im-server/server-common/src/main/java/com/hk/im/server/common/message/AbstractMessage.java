package com.hk.im.server.common.message;

import com.hk.im.domain.bo.MessageBO;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : AbstractMessage
 * @date : 2022/12/28 0:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public abstract class AbstractMessage implements Serializable {

    protected int sequenceId;

    protected int messageType;

    protected MessageBO messageBO;

    public abstract int getMessageType();

}
