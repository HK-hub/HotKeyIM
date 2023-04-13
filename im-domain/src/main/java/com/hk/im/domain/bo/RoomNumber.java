package com.hk.im.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : HK意境
 * @ClassName : RoomNumber
 * @date : 2023/3/23 9:07
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class RoomNumber implements Serializable {

    // 房间号
    private Integer number;

    // 是否在使用中
    private Boolean inUse = Boolean.FALSE;

    // 持有者id
    private Long hostId;


}
