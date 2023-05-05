package com.hk.im.service.service;

import cn.hutool.core.util.NumberUtil;
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
import com.hk.im.domain.constant.GroupMemberConstants;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.HistoryRecordsDTO;
import com.hk.im.domain.dto.LatestMessageRecordDTO;
import com.hk.im.domain.dto.RevokeMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.message.chat.AttachmentMessage;
import com.hk.im.domain.message.chat.ImageMessage;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.po.PrivateRecordsSelectPO;
import com.hk.im.domain.request.*;
import com.hk.im.domain.request.message.RevokeMessageRequest;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.domain.vo.message.RevokeMessageVO;
import com.hk.im.infrastructure.event.message.event.RevokeChatMessageEvent;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.manager.UserManager;
import com.hk.im.infrastructure.mapper.MessageFlowMapper;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import com.hk.im.service.worker.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private LocationMessageWorker locationMessageWorker;
    @Resource
    private ApplicationContext applicationContext;


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
                .sorted(Comparator.comparing(MessageVO::getSequence)).collect(Collectors.toList());

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
                .sorted(Comparator.comparing(MessageVO::getSequence)).collect(Collectors.toList());

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
     * 转换为消息VO 对象
     * @param messageFlow
     * @return
     */
    @Override
    public MessageVO convertToMessageVO(MessageFlow messageFlow) {

        // 查询消息体
        ChatMessage message = this.chatMessageService.getById(messageFlow.getMessageId());
        // 转换为 MessageBO
        MessageBO messageBO = MessageMapStructure.INSTANCE.toBO(messageFlow, message);
        // 转换为MessageVO
        MessageVO messageVO = MessageMapStructure.INSTANCE.boToVO(messageBO);

        // 查询发送者
        UserVO senderVO = this.userManager.findUserAndInfo(messageFlow.getSenderId());
        // 判断是群聊还是私聊
        if (CommunicationConstants.SessionType.PRIVATE.ordinal() == messageFlow.getChatType()) {
            // 私聊:如果是私聊或者@群员消息->计算头像，id
            UserVO friendVO = this.userManager.findUserAndInfo(messageFlow.getReceiverId());
            messageVO.computedPrivateMessageVO(senderVO, friendVO, null);
        } else if (CommunicationConstants.SessionType.GROUP.ordinal() == messageFlow.getChatType()) {
            // 群聊: 查询发送者群员
            GroupMember groupMember = this.groupMemberService.getTheGroupMember(messageVO.getReceiverId(), messageVO.getSenderId());
            messageVO.computedPrivateMessageVO(senderVO, null, groupMember);
        }
        return messageVO;
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

        // return this.videoMessageWorker.inviteVideoCall(request);
        return null;
    }


    /**
     * 发送位置消息
     * @param request
     * @return
     */
    @Override
    public ResponseResult sendLocationMessage(LocationMessageRequest request) {

        return this.locationMessageWorker.sendMessage(request);
    }


    /**
     * 确认消息
     * @param senderId
     * @param receiverId
     * @param ackMessageIdList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<List<Long>> ackChatMessage(Long senderId, Long receiverId, List<Long> ackMessageIdList) {

        // 查询消息
        List<MessageFlow> messageList = this.lambdaQuery()
                .in(MessageFlow::getMessageId, ackMessageIdList)
                .list();

        // 过滤出属于发送者-接收者的消息：我确认收到你的消息，那么消息接收者是我，消息发送者是你
        messageList = messageList.stream().filter(message -> message.getSenderId().equals(receiverId) && message.getReceiverId().equals(senderId)).collect(Collectors.toList());

        // 确认消息接收状态
        messageList.forEach(message -> message.setSignFlag(MessageConstants.SignStatsEnum.READ.ordinal()));

        // 更新
        boolean updateBatchById = this.updateBatchById(messageList);
        if (BooleanUtils.isFalse(updateBatchById)) {
            ResponseResult.FAIL();
        }

        // 回执确认收到的消息
        return ResponseResult.SUCCESS(ackMessageIdList);
    }


    /**
     * 发起视频通话邀请
     * @param request
     * @return
     */
    @Override
    public ResponseResult sendVideoInviteMessage(InviteVideoCallInviteRequest request) {

        return this.videoMessageWorker.inviteVideoCall(request);
    }


    /**
     * 撤回消息
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult revokeMessage(RevokeMessageRequest request) {
        
        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getMessageId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 素材
        Long messageId = request.getMessageId();
        Long handlerId = request.getHandlerId();
        if (Objects.isNull(handlerId)) {
            handlerId = UserContextHolder.get().getId();
        }

        // 校验消息是否存在
        // ChatMessage message = this.chatMessageService.getById(messageId);
        MessageFlow messageFlow = this.lambdaQuery()
                .eq(MessageFlow::getMessageId, messageId)
                .eq(MessageFlow::getDeleted, Boolean.FALSE)
                .one();
        if (Objects.isNull(messageFlow)) {
            // 消息不存在撤回失败
            return ResponseResult.FAIL().setMessage("消息不存在!");
        }

        // 查询消息实体
        ChatMessage message = this.chatMessageService.getById(messageId);

        // 消息类型
        Integer chatType = messageFlow.getChatType();
        CommunicationConstants.SessionType talkType = CommunicationConstants.SessionType.values()[chatType];

        // 撤回者昵称
        String revokeUserNickname = null;
        // 根据消息聊天类型进行撤回
        if (talkType == CommunicationConstants.SessionType.PRIVATE) {
            // 私聊：检查处理的消息发送者是否自己的
            boolean myMessage = messageFlow.getSenderId().equals(handlerId);
            if (BooleanUtils.isFalse(myMessage)) {
                // 不是自己的消息
                return ResponseResult.FAIL().setMessage("无法撤回对方发送的消息!");
            }

            // 发送时间是否超过2分钟
            LocalDateTime createTime = messageFlow.getCreateTime();
            LocalDateTime now = LocalDateTime.now().minusMinutes(2);

            if (now.isAfter(createTime)) {
                // 超过2分钟了
                return ResponseResult.FAIL().setMessage("消息发送超过2分钟,无法撤回!");
            }

            // 自己的消息执行撤回逻辑：查询撤回者的昵称
            revokeUserNickname = "你";

        } else if (talkType == CommunicationConstants.SessionType.GROUP) {
            // 群聊： 处理人员是否本人或具有管理员权限
            Long groupId = messageFlow.getReceiverId();
            GroupMember member = this.groupMemberService.getTheGroupMember(groupId, handlerId);
            if (Objects.isNull(member)) {
                // 非本群成员
                return ResponseResult.FAIL().setMessage("您非本群成员,无权撤回!");
            }

            // 校验本人
            boolean myMessage = Objects.equals(messageFlow.getSenderId(), handlerId);

            // 时间校验
            LocalDateTime createTime = messageFlow.getCreateTime();
            LocalDateTime now = LocalDateTime.now().minusMinutes(2);
            boolean after = now.isAfter(createTime);

            // 校验权限
            GroupMemberConstants.GroupMemberRole role = GroupMemberConstants.GroupMemberRole.values()[member.getMemberRole()];
            boolean hasPermission = role == GroupMemberConstants.GroupMemberRole.ADMIN || role == GroupMemberConstants.GroupMemberRole.MASTER;

            // 是否有权撤回: 本人 并且可以撤回 或者 有权限撤回
            boolean enableRevoke = (myMessage && after) || hasPermission;
            if (BooleanUtils.isFalse(enableRevoke)) {
                // 无权，超时
                return ResponseResult.FAIL().setMessage("抱歉您无法撤回消息!");
            }

            // 查询撤回者的昵称
            GroupMember groupMember = this.groupMemberService.getTheGroupMember(messageFlow.getReceiverId(), handlerId);
            revokeUserNickname = groupMember.getMemberRemarkName();
        }

        // 执行撤回消息逻辑
        RevokeMessageExtra extra = new RevokeMessageExtra()
                .setHandlerId(handlerId).setUsername(revokeUserNickname)
                .setCreateTime(LocalDateTime.now());
        message.setExtra(extra);
        messageFlow.setRevoke(Boolean.TRUE)
                .setSignFlag(MessageConstants.SignStatsEnum.REVOKE.ordinal());
        // 更新
        boolean updateFlow = this.updateById(messageFlow);
        boolean updateMessage = this.chatMessageService.updateById(message);
        if (BooleanUtils.isFalse(updateFlow) || BooleanUtils.isFalse(updateMessage)) {
            // 撤回失败
            return ResponseResult.FAIL();
        }

        // 发送撤回消息事件
        RevokeMessageVO revokeMessageVO = new RevokeMessageVO();
        revokeMessageVO.setFlow(messageFlow)
                .setRevoker(handlerId).setNickname(revokeUserNickname);
        // 发送事件
        this.applicationContext.publishEvent(new RevokeChatMessageEvent(this, revokeMessageVO));

        // 响应结果
        return ResponseResult.SUCCESS();
    }


}




