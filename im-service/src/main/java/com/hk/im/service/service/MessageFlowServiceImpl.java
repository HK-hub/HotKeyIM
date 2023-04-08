package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hk.im.client.service.*;
import com.hk.im.common.consntant.MinioConstant;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.HistoryRecordsDTO;
import com.hk.im.domain.dto.LatestMessageRecordDTO;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.message.chat.AttachmentMessage;
import com.hk.im.domain.message.chat.ImageMessage;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.po.PrivateRecordsSelectPO;
import com.hk.im.domain.request.CodeMessageRequest;
import com.hk.im.domain.request.InviteVideoCallRequest;
import com.hk.im.domain.request.TalkRecordsRequest;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.manager.UserManager;
import com.hk.im.infrastructure.mapper.MessageFlowMapper;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import com.hk.im.service.worker.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
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
    private GroupMemberService groupMemberService;
    @Resource
    private ChatMessageService chatMessageService;
    @Resource
    private UserManager userManager;
    @Resource
    private TextMessageWorker textMessageWorker;
    @Resource
    private ImageMessageWorker imageMessageWorker;
    @Resource
    private AttachmentMessageWorker attachmentMessageWorker;
    @Resource
    private CodeMessageWorker codeMessageWorker;
    @Resource
    private VideoMessageWorker videoMessageWorker;


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
                    // 如果是私聊或者@群员消息->计算头像，id
                    if (CommunicationConstants.SessionType.PRIVATE.ordinal() == talkType) {
                        // 私聊
                        messageVO.computedPrivateMessageVO(userVO, friendVO, null);
                    } else if (CommunicationConstants.SessionType.GROUP.ordinal() == talkType) {
                        // 群聊
                        // 查询发送者群员
                        GroupMember groupMember = this.groupMemberService.getTheGroupMember(messageVO.getReceiverId(), messageVO.getSenderId());
                        messageVO.computedPrivateMessageVO(userVO, null, groupMember);
                    }
                    return messageVO;
                })
                // 排序：按照 sequence 排序
                .sorted(Comparator.comparing(MessageVO::getSequence)).toList();

        // 获取最小的 sequence
        MessageFlow minMessage = new MessageFlow().setSequence(0L).setMessageId(0L);
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
        if (StringUtils.isEmpty(request.getSenderId())) {
            // 没有选择发送用户：发送者为登录用户
            request.setSenderId(""+UserContextHolder.get().getId());
        }
        Long senderId = Long.valueOf(request.getSenderId());
        Long receiverId = Long.valueOf(request.getReceiverId());
        // 这里只按照分页查找
        Integer currentPage = request.getCurrentPage();
        Integer needPages = request.getNeedPages();
        Integer direction = request.getDirection();
        Integer limit = Objects.isNull(request.getLimit()) ? MessageConstants.DEFAULT_RECORDS_LIMIT : request.getLimit();

        // 查询消息成员信息
        // 查询用户
        UserVO userVO = this.userManager.findUserAndInfo(senderId);
        UserVO friendVO = null;

        // 查询消息记录
        List<MessageFlow> messageFlowList = new ArrayList<>();

        // 查找指定消息类型判断
        if (CollectionUtils.isNotEmpty(request.getMsgTypes()) && request.getMsgTypes().size() == 1) {
            // 判断是否为全部=0
            if (request.getMsgTypes().get(0) == 0) {
                request.setMsgTypes(Collections.emptyList());
            }
        }

        // 选择查找消息类型
        Integer talkType = request.getTalkType();
        if (CommunicationConstants.SessionType.PRIVATE.ordinal() == talkType) {
            // 好友私聊
            messageFlowList =  this.messageFlowMapper.selectPrivateHistoryRecords(request);
            friendVO = this.userManager.findUserAndInfo(receiverId);

        } else if (CommunicationConstants.SessionType.GROUP.ordinal() == talkType) {
            // 群聊
            messageFlowList = this.messageFlowMapper.selectGroupHistoryRecords(request);
        }

        // 根据锚点进行前后查询
        /*String anchor = request.getAnchor();
        if (StringUtils.isEmpty(anchor)) {
            // 没有设置锚点：可能没有聊天记录，可能会话已经被删除，则从数据库获取最新消息
            // TODO
            MessageFlow latestMessage = this.messageFlowMapper.getPrivateLatestMessageRecord(request.getSenderId(), receiverId);
        }
        MessageFlow anchorMessage = this.getById(request.getAnchor());*/

        // 查询消息体
        // 转换为MessageB: 查询消息体
        UserVO finalFriendVO = friendVO;
        List<MessageVO> messageVOList = messageFlowList.stream().map(flow -> {
                    // 查询消息体
                    ChatMessage message = this.chatMessageService.getById(flow.getMessageId());
                    // 转换为 MessageBO
                    MessageBO messageBO = MessageMapStructure.INSTANCE.toBO(flow, message);
                    // 转换为MessageVO
                    MessageVO messageVO = MessageMapStructure.INSTANCE.boToVO(messageBO);
                    // 如果是私聊或者@群员消息->计算头像，id
                    if (CommunicationConstants.SessionType.PRIVATE.ordinal() == talkType) {
                        // 私聊
                        messageVO.computedPrivateMessageVO(userVO, finalFriendVO, null);
                    } else if (CommunicationConstants.SessionType.GROUP.ordinal() == talkType) {
                        // 群聊
                        // 查询发送者群员
                        GroupMember groupMember = this.groupMemberService.getTheGroupMember(messageVO.getReceiverId(), messageVO.getSenderId());
                        messageVO.computedPrivateMessageVO(userVO, null, groupMember);
                    }
                    return messageVO;
                })
                // 排序：按照 sequence 排序
                .sorted(Comparator.comparing(MessageVO::getSequence)).toList();

        // 获取最小的 sequence
        MessageFlow minMessage = new MessageFlow().setSequence(0L).setMessageId(0L);
        Optional<MessageFlow> minOptional = messageFlowList.stream()
                .min(Comparator.comparingLong(MessageFlow::getSequence));
        // 存在最小消息记录
        if (minOptional.isPresent()) {
            minMessage = minOptional.get();
        }

        // 响应数据
        HistoryRecordsDTO recordDTO = new HistoryRecordsDTO().setMessageVOList(messageVOList)
                .setLimit(limit)
                .setSequence(minMessage.getSequence())
                .setAnchorId(minMessage.getMessageId());

        return ResponseResult.SUCCESS(recordDTO);
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
        return this.textMessageWorker.sendMessage(message);
    }

    /**
     * 发送图片消息
     * @param request
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult sendImageMessage(ImageMessage request) {

        return this.imageMessageWorker.sendMessage(request);
    }


    /**
     * 发送附件，文件消息
     * @param request
     * @return
     */
    @Override
    public ResponseResult sendAttachmentMessage(AttachmentMessage request) {

        return this.attachmentMessageWorker.sendMessage(request);
    }


    /**
     * 发送代码消息
     * @param request
     * @return
     */
    @Override
    public ResponseResult sendCodeMessage(CodeMessageRequest request) {

        return this.codeMessageWorker.sendMessage(request);
    }


    /**
     * 发起视频通话
     * @param request
     * @return
     */
    @Override
    public ResponseResult sendVideoMessage(InviteVideoCallRequest request) {

        return this.videoMessageWorker.inviteVideoCall(request);
    }


}




