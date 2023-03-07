package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.*;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.common.resp.ResultEntity;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.LatestMessageRecordDTO;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.po.PrivateRecordsSelectPO;
import com.hk.im.domain.request.TalkRecordsRequest;
import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.manager.UserManager;
import com.hk.im.infrastructure.mapper.MessageFlowMapper;
import com.hk.im.infrastructure.mapper.UserInfoMapper;
import com.hk.im.infrastructure.mapper.UserMapper;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author : HK意境
 * @ClassName : MessageFlowServiceImpl
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
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private UserManager userManager;
    @Resource
    private FriendService friendService;


    /**
     * 获取会话最大消息Sequence
     *
     * @param senderId
     * @param receiverId
     *
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
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult getLatestTalkRecords(TalkRecordsRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getReceiverId())
                || Objects.isNull(request.getTalkType()) || Objects.isNull(request.getLimit()) || Objects.isNull(request.getAnchor());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 需要获取的聊天记录数
        Integer limit = request.getLimit();
        // 检验登录用户
        Long senderId = null;
        if (StringUtils.isEmpty(request.getSenderId())) {
            // 没有传输 userId, 通过 UserContextHolder 获取
            request.setSenderId(String.valueOf(UserContextHolder.get().getId()));
        }
        senderId = Long.parseLong(request.getSenderId());
        Long receiverId = Long.valueOf(request.getReceiverId());

        // 查询用户
        UserVO userVO = this.userManager.findUserAndInfo(senderId);
        UserVO friendVO = this.userManager.findUserAndInfo(receiverId);

        // 获取聊天类型
        Integer talkType = request.getTalkType();

        // 根据聊天类型获取聊天记录
        List<MessageFlow> messageFlowList = Collections.emptyList();
        PrivateRecordsSelectPO selectPO = new PrivateRecordsSelectPO().setSenderId(senderId)
                .setReceiverId(receiverId)
                .setSequence(request.getSequence()).setLimit(limit);
        if (CommunicationConstants.SessionType.PRIVATE.ordinal() == talkType) {
            // 好友私聊
            // 获取聊天记录流水
            messageFlowList = this.messageFlowMapper.selectPrivateRecordsByAnchor(selectPO);
        } else if (CommunicationConstants.SessionType.GROUP.ordinal() == talkType) {
            // 群聊
            // 获取聊天记录流水
            messageFlowList = this.messageFlowMapper.selectGroupRecordsByAnchor(selectPO);
        }
        // 转换为MessageB: 查询消息体
        List<MessageVO> messageVOList = messageFlowList.stream().map(flow -> {
                    // 查询消息体
                    ChatMessage message = this.chatMessageService.getById(flow.getMessageId());
                    // 转换为 MessageBO
                    MessageBO messageBO = MessageMapStructure.INSTANCE.toBO(flow, message);
                    // 转换为MessageVO
                    MessageVO messageVO = MessageMapStructure.INSTANCE.boToVO(messageBO);
                    // 计算头像，id
                    return messageVO.computedPrivateMessageVO(userVO, friendVO);
                })
                // 排序：按照 sequence 排序
                .sorted(Comparator.comparing(MessageVO::getSequence)).toList();

        // 获取最小的 sequence
        MessageFlow minMessage = new MessageFlow().setSequence(0L).setMessageId(0L);
        ;
        Optional<MessageFlow> minOptional = messageFlowList.stream()
                .min(Comparator.comparingLong(MessageFlow::getSequence));
        // 存在最小消息记录
        if (minOptional.isPresent()) {
            minMessage = minOptional.get();
        }

        // 响应数据
        LatestMessageRecordDTO recordDTO = new LatestMessageRecordDTO().setMessageVOList(messageVOList)
                .setLimit(limit)
                .setSequence(minMessage.getSequence())
                .setAnchorId(minMessage.getMessageId());
        return ResponseResult.SUCCESS(recordDTO);
    }

    /**
     * 分页获取聊天记录
     *
     * @param request
     *
     * @return TODO
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


    /**
     * 发送文本消息
     *
     * @param message
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized ResponseResult sendTextMessage(TextMessage message) {

        // 参数校验
        boolean preCheck = Objects.isNull(message) || StringUtils.isEmpty(message.getSenderId()) || Objects.isNull(message.getText()) ||
                StringUtils.isEmpty(message.getReceiverId()) || Objects.isNull(message.getTalkType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 消息参数校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 校验消息类型
        Integer chatMessageType = message.getChatMessageType();
        if (MessageConstants.ChatMessageType.TEXT.ordinal() != chatMessageType) {
            // 不是文本消息类型
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 素材准备
        Long senderId = Long.valueOf(message.getSenderId());
        Long receiverId = Long.valueOf(message.getReceiverId());
        String text = message.getText();
        Integer talkType = message.getTalkType();

        // 保存消息
        ChatMessage chatMessage = new ChatMessage()
                // 消息内容
                .setContent(text)
                // 消息特性
                .setMessageFeature(MessageConstants.MessageFeature.DEFAULT.ordinal())
                // 消息类型
                .setMessageType(MessageConstants.ChatMessageType.TEXT.ordinal());
        // 获取消息序列号
        ResponseResult sequenceResult = this.sequenceService.nextId(senderId, receiverId, talkType);
        if (BooleanUtils.isFalse(sequenceResult.isSuccess())) {
            // 获取序列号失败
            return ResponseResult.FAIL().setResultCode(ResultCode.SERVER_BUSY);
        }

        // 设置消息序列号
        chatMessage.setSequence((Long) sequenceResult.getData());
        // 消息流水
        MessageFlow messageFlow = new MessageFlow()
                .setSenderId(senderId).setReceiverId(receiverId)
                .setMessageType(chatMessage.getMessageType()).setChatType(talkType)
                .setSequence(chatMessage.getSequence())
                // 消息签收状态
                .setSignFlag(MessageConstants.SignStatsEnum.UNREAD.ordinal())
                // 消息发送状态
                .setSendStatus(MessageConstants.SendStatusEnum.SENDING.ordinal())
                .setDeleted(Boolean.FALSE);

        // 保存消息和消息流水
        MessageBO messageBO = doSaveMessageAndFlow(chatMessage, messageFlow);
        // 判断消息发送是否成功
        if (Objects.isNull(messageBO)) {
            // 消息发送失败: 设置草稿->
            messageFlow.setSendStatus(MessageConstants.SendStatusEnum.FAIL.ordinal());
            messageBO = MessageMapStructure.INSTANCE.toBO(messageFlow, chatMessage);
            // TODO 发送消息保存事件
            this.applicationContext.publishEvent(new SendChatMessageEvent(this, messageBO));
            // 响应
            return ResponseResult.FAIL().setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 发送消息保存事件
        this.applicationContext.publishEvent(new SendChatMessageEvent(this, messageBO));

        // 发送消息成功
        return ResponseResult.SUCCESS(messageBO).setMessage("消息发送成功!");
    }


    /**
     * 保存消息和消息流水
     *
     * @param message
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageBO doSaveMessageAndFlow(ChatMessage message, MessageFlow flow) {

        // 保存消息
        boolean save = this.chatMessageService.save(message);
        if (BooleanUtils.isFalse(save)) {
            // 保存消息失败
            return null;
        }

        // 设置消息流水
        flow.setMessageId(message.getId())
                .setCreateTime(message.getCreateTime())
                .setUpdateTime(message.getUpdateTime())
                .setSendStatus(MessageConstants.SendStatusEnum.SENDED.ordinal());
        Integer chatType = flow.getChatType();
        if (chatType == CommunicationConstants.SessionType.GROUP.ordinal()) {
            // 群聊类型
            flow.setGroupId(flow.getReceiverId());
        }

        // 保存消息流水
        boolean flowSave = this.save(flow);
        if (BooleanUtils.isFalse(flowSave)) {
            // 保存失败
            return null;
        }


        // 更新会话状态

        // 响应DTO
        return MessageMapStructure.INSTANCE.toBO(flow, message);
    }


}




