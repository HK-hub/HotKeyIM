package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.request.TalkRecordsRequest;
import com.hk.im.infrastructure.mapper.MessageFlowMapper;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import com.hk.im.service.service.ChatMessageService;
import com.hk.im.service.service.GroupService;
import com.hk.im.service.service.MessageFlowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : MessageFlowServiceImpl
 * @author : HK意境
 * @date : 2023/2/22 10:15
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class MessageFlowServiceImpl extends ServiceImpl<MessageFlowMapper, MessageFlow> implements MessageFlowService {

    @Resource
    private MessageFlowMapper messageFlowMapper;
    @Resource
    private GroupService groupService;
    @Resource
    private ChatMessageService chatMessageService;


    /**
     * 获取会话最大消息Sequence
     * @param senderId
     * @param receiverId
     * @return
     */
    @Override
    public MessageFlow getCommunicationMaxMessageSequence(Long senderId, Long receiverId) {

        // 根据 receiverId 是否为群群聊
        Group group = this.groupService.getById(receiverId);
        if (Objects.nonNull(group)) {
            // 群聊，只需要获取receiverId 最大消息
            MessageFlow messageFlow = this.messageFlowMapper.selectGroupMaxMessageSequence(senderId, receiverId);
            // 响应处理
            return messageFlow;
        }

        // 好友私聊, 需要查询双向消息中较大的消息ID
        MessageFlow messageFlow = this.messageFlowMapper.selectPrivateMaxMessageSequence(senderId, receiverId);
        // 响应处理
        return messageFlow;
    }


    /**
     * 获取最新聊天信息记录
     * @param request
     * @return
     */
    @Override
    public ResponseResult getLatestTalkRecords(TalkRecordsRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getReceiverId())
                ||Objects.isNull(request.getTalkType()) || Objects.isNull(request.getLimit());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 需要获取的聊天记录数
        Integer limit = request.getLimit();
        // 检验登录用户
        String userId = request.getUserId();
        if (StringUtils.isEmpty(userId)) {
            // 没有传输 userId, 通过 UserContextHolder 获取
            userId = String.valueOf(UserContextHolder.get().getId());
        }
        String receiverId = request.getReceiverId();
        // 获取聊天类型
        Integer talkType = request.getTalkType();

        List<MessageFlow> messageFlowList = Collections.emptyList();
        // 根据聊天类型获取聊天记录
        if (CommunicationConstants.SessionType.PRIVATE.ordinal() == talkType) {
            // 好友私聊
            // 获取聊天记录流水
            messageFlowList = this.lambdaQuery()
                    .eq(MessageFlow::getReceiverId, receiverId)
                    .eq(MessageFlow::getSenderId, userId)
                    .orderByDesc(MessageFlow::getSequence)
                    .last(" limit " + limit)
                    .list();
        } else if (CommunicationConstants.SessionType.GROUP.ordinal() == talkType) {
            // 群聊
            // 获取聊天记录流水
            messageFlowList = this.lambdaQuery()
                    .eq(MessageFlow::getGroupId, receiverId)
                    .or(wrapper -> {
                        wrapper.eq(MessageFlow::getReceiverId, receiverId);
                    })
                    .orderByDesc(MessageFlow::getSequence)
                    .last(" limit " + limit)
                    .list();
        }

        // 转换为MessageB: 查询消息体
        List<MessageBO> messageBOList = messageFlowList.stream().map(flow -> {
            // 查询消息体
            ChatMessage message = this.chatMessageService.getById(flow.getMessageId());
            // 转换为 MessageBO
            MessageBO messageBO = MessageMapStructure.INSTANCE.toBO(flow, message);
            return messageBO;
        }).toList();


        // 响应数据
        return ResponseResult.SUCCESS(messageBOList);
    }


    /**
     * 分页获取聊天记录
     * @param request
     * @return
     * TODO
     */
    @Override
    public ResponseResult getTalkRecordsByPage(TalkRecordsRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getReceiverId()) || Objects.isNull(request.getLimit())
                || Objects.isNull(request.getCurrentPage()) || Objects.isNull(request.getTalkType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL("获取聊天记录失败!");
        }

        // 获取用户
        String receiverId = request.getReceiverId();
        // 这里只按照分页查找
        Integer currentPage = request.getCurrentPage();
        Integer needPages = request.getNeedPages();
        Integer direction = request.getDirection();
        Integer limit = Objects.isNull(request.getLimit()) ? MessageConstants.DEFAULT_RECORDS_LIMIT : request.getLimit();

        // 封装查询条件
        LambdaQueryChainWrapper<MessageFlow> queryWrapper = this.lambdaQuery();

        // 选择查找消息类型
        Integer talkType = request.getTalkType();
        if (CommunicationConstants.SessionType.PRIVATE.ordinal() == talkType) {
            // 好友私聊
            // 参数校验
            String senderId = request.getSenderId();
            if (StringUtils.isEmpty(senderId)) {
                return ResponseResult.FAIL("获取聊天记录失败!");
            }

            queryWrapper.eq(MessageFlow::getReceiverId, receiverId)
                    .eq(MessageFlow::getSenderId, senderId);

        } else if (CommunicationConstants.SessionType.GROUP.ordinal() == talkType) {
            // 群聊
            queryWrapper.eq(MessageFlow::getReceiverId, receiverId);
        }

        // 根据锚点进行前后查询
        String anchor = request.getAnchor();
        if (StringUtils.isEmpty(anchor)) {
            // 没有设置锚点：可能没有聊天记录，可能会话已经被删除，则从数据库获取最新消息
            // TODO
            MessageFlow latestMessage = this.messageFlowMapper.getPrivateLatestMessageRecord(request.getSenderId(), receiverId);
        }
        MessageFlow anchorMessage = this.getById(request.getAnchor());

        // 分页查找
        Page<MessageFlow> page = queryWrapper.page(Page.of(currentPage, limit, false));

        return null;
    }
}




