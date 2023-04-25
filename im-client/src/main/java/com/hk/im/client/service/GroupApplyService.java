package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.GroupApply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.request.ApplyHandleRequest;
import com.hk.im.domain.request.CreateGroupApplyRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

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
    public ResponseResult createGroupApply(CreateGroupApplyRequest request) throws Exception;


    /**
     * 获取用户管理群聊的加群申请
     * @param userId
     * @return
     */
    public ResponseResult getUserGroupApplyList(String userId);

    /**
     * 获取指定群聊加群申请
     * @param userId
     * @param groupId
     * @return
     */
    public ResponseResult getTheGroupApplyList(Long userId, Long groupId);


    /**
     * 处理群聊申请
     * @param request
     * @return
     */
    public ResponseResult handleGroupApply(ApplyHandleRequest request);

    /**
     * 获取群聊加群申请方式设置
     * @param groupId
     * @return
     */
    public ResponseResult getGroupApplySetting(Long groupId);
}
