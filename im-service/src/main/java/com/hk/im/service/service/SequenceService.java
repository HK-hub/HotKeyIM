package com.hk.im.service.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Sequence;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * @ClassName : SequenceService
 * @author : HK意境
 * @date : 2023/1/26 18:45
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface SequenceService extends IService<Sequence> {

    /**
     * 获取一个会话发号器
     * @param communicationId
     * @param participantId
     * @return
     */
    public ResponseResult getSequence(Long communicationId, Long participantId);


    /**
     * 创建会话器
     * @param communicationId
     * @param participantId
     * @return
     */
    public ResponseResult createSequence(Long communicationId, Long participantId);

    /**
     * 获取会话的下一个ID
     * @param communicationId
     * @param participantId
     * @return
     */
    public ResponseResult nextId(Long communicationId, Long participantId);

}
