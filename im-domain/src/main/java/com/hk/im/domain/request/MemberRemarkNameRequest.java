package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : MemberRemarkNameRequest
 * @date : 2023/1/22 19:35
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MemberRemarkNameRequest {

    private String groupId;

    private String memberId;

    private String operatorId;

    private String newRemarkName;


}
