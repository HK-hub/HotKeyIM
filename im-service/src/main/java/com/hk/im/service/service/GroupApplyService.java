package com.hk.im.service.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.GroupApply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.request.ApplyHandleRequest;
import com.hk.im.domain.request.CreateGroupApplyRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName : GroupApplyService
 * @author : HK意境
 * @date : 2023/2/14 11:39
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface GroupApplyService extends IService<GroupApply> {


    /**
     * 创建加群申请
     * @param request
     * @return
     */
    ResponseResult createGroupApply(CreateGroupApplyRequest request);


    /**
     * 获取用户管理群聊的加群申请
     * @param userId
     * @return
     */
    ResponseResult getUserGroupApplyList(String userId);


    /**
     * 处理群聊申请
     * @param request
     * @return
     */
    ResponseResult handleGroupApply(ApplyHandleRequest request);
}
