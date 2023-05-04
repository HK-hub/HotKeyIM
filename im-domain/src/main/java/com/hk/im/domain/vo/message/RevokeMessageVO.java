package com.hk.im.domain.vo.message;

import com.hk.im.domain.entity.MessageFlow;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : RevokeMessageVO
 * @date : 2023/5/4 20:35
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class RevokeMessageVO {

    private MessageFlow flow;

    // 撤回者的昵称
    private String nickname;

    // 撤回者
    private Long revoker;

}
