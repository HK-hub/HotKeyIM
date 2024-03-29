package com.hk.im.service.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.AuthorizationService;
import com.hk.im.client.service.ChatCommunicationService;
import com.hk.im.client.service.FriendService;
import com.hk.im.client.service.GroupService;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.request.ClearUnreadRequest;
import com.hk.im.domain.request.CreateCommunicationRequest;
import com.hk.im.domain.request.TopTalkRequest;
import com.hk.im.domain.request.talk.RemoveTalkRequest;
import com.hk.im.domain.request.talk.SetTalkDisturbRequest;
import com.hk.im.domain.vo.ChatCommunicationVO;
import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.infrastructure.mapper.ChatCommunicationMapper;
import com.hk.im.infrastructure.mapstruct.CommunicationMapStructure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : ChatCommunicationServiceImpl
 * @date : 2023/2/10 18:33
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class ChatCommunicationServiceImpl extends ServiceImpl<ChatCommunicationMapper, ChatCommunication>
        implements ChatCommunicationService {

    @Resource
    private ChatCommunicationMapper chatCommunicationMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private FriendService friendService;
    @Resource
    private GroupService groupService;
    @Resource
    private AuthorizationService authorizationService;

    /**
     * 创建会话
     *
     * @param request: type 标识会话类型
     *
     * @return
     */
    @Override
    public ResponseResult createChatCommunication(CreateCommunicationRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getType()) || StringUtils.isEmpty(request.getReceiverId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("参数校验失败!");
        }

        // 校验用户
        String userId = request.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return ResponseResult.FAIL("参数校验失败!").setResultCode(ResultCode.UNAUTHENTICATED);
        }

        // 数据准备
        Long receiverId = Long.valueOf(request.getReceiverId());
        Long senderId = Long.valueOf(userId);

        // 获取会话，判断会话是否存在
        ChatCommunication talkResult = this.existsChatCommunication(senderId,
                receiverId);
        if (Objects.nonNull(talkResult)) {
            // 会话已经存在
            // 判断是否逻辑删除了会话
            if (BooleanUtils.isTrue(talkResult.getDeleted())) {
                // 逻辑删除了，需要进行恢复
                this.updateById(talkResult.setDeleted(Boolean.FALSE));
            }
            return ResponseResult.SUCCESS();
        }

        // 会话不存在，创建
        Integer type = request.getType();
        ChatCommunication talkOne = new ChatCommunication()
                .setBelongUserId(senderId)
                .setSenderId(senderId)
                .setReceiverId(receiverId)
                .setSessionType(type)
                .setRobot(false).setTop(false).setDisturb(false).setOnline(false)
                .setUnreadCount(0);

        // 设置会话类型
        CommunicationConstants.SessionType sessionType = CommunicationConstants.SessionType.values()[type];
        if (sessionType == CommunicationConstants.SessionType.GROUP) {
            talkOne.setGroupId(receiverId);
        } else if (sessionType == CommunicationConstants.SessionType.PRIVATE){
            // 好友会话类型
            // 查看是否存在
            ChatCommunication talkTwo = this.existsChatCommunication(receiverId, senderId);
            if (Objects.isNull(talkTwo)) {
                // 会话不存在，创建会话
                talkTwo = new ChatCommunication()
                        .setBelongUserId(receiverId)
                        .setSenderId(receiverId)
                        .setReceiverId(senderId)
                        .setSessionType(type);
                this.save(talkTwo);
            }
        }

        // 保存
        boolean save = this.save(talkOne);
        if (BooleanUtils.isFalse(save)) {
            // 保存失败
            return ResponseResult.FAIL();
        }

        // 保存成功-缓存到redis
        String name = senderId < receiverId ? senderId + "-" + receiverId : receiverId + "-" + senderId;
        // 根据会话类型进行设置
        /*if (type == 1) {
            // 私聊
            name = senderId < receiverId ? senderId + "-" + receiverId : receiverId + "-" + senderId;

        } else if (type == 2) {
            // 群聊
            name = senderId + "-" + receiverId;
        }*/
        if (sessionType == CommunicationConstants.SessionType.GROUP) {
            name = request.getReceiverId();
        }
        String key = RedisConstants.COMMUNICATION_KEY + RedisConstants.SEQUENCE_KEY + name;

        // 设置到 redis 缓存: 永不过期
        this.stringRedisTemplate.opsForValue().set(key, String.valueOf(0), 120, TimeUnit.MINUTES);

        // 构造响应数据
        ChatCommunicationVO communicationVO = this.convertToVO(Long.valueOf(userId), talkOne);

        return ResponseResult.SUCCESS(communicationVO);
    }


    /**
     * 转换为 VO
     * @param userId
     * @param talk
     * @return
     */
    private ChatCommunicationVO convertToVO(Long userId, ChatCommunication talk) {
        // 数据容器
        FriendVO friend = null;
        GroupVO group = null;
        Integer sessionType = talk.getSessionType();
        if (sessionType == CommunicationConstants.SessionType.PRIVATE.ordinal()) {
            // 好友私聊
            friend = this.friendService.getUserFriendById(userId, talk.getReceiverId());
        } else if (sessionType == CommunicationConstants.SessionType.GROUP.ordinal()) {
            // 群聊
            group = this.groupService.getGroupVOById(talk.getReceiverId());
        }
        // 组合
        ChatCommunicationVO vo = CommunicationMapStructure.INSTANCE.toVO(talk, friend, group);

        // 设置其余属性
        if (sessionType == CommunicationConstants.SessionType.PRIVATE.ordinal()) {
            // 好友
            vo.setReceiverName(StringUtils.isEmpty(vo.getFriendVO().getRemarkName())
                    ? vo.getFriendVO().getNickname() : vo.getFriendVO().getRemarkName());
            // 头像
            vo.setAvatar(vo.getFriendVO().getAvatar());
        } else if (sessionType == CommunicationConstants.SessionType.GROUP.ordinal()) {
            // 群聊
            vo.setReceiverName(vo.getGroupVO().getGroupName());
            // 头像
            vo.setAvatar(vo.getGroupVO().getGroupAvatar());
        }

        return vo;
    }



    /**
     * 判断会话是否存在
     * @param senderId
     * @param receiverId
     * @return
     */
    @Override
    public ChatCommunication existsChatCommunication(Long senderId, Long receiverId) {
        // 判断是否存在
        ChatCommunication talk = this.lambdaQuery()
                .eq(ChatCommunication::getSenderId, senderId)
                .eq(ChatCommunication::getReceiverId, receiverId)
                .one();

        return talk;
    }

    /**
     * 获取会话
     *
     * @param senderId
     * @param receiverId
     *
     * @return
     */
    @Override
    public ResponseResult getChatCommunication(Long senderId, Long receiverId) {

        ChatCommunication communication = null;
        // 判断当前会话对象是否为自己
        if (Objects.equals(senderId, receiverId)) {
            // 发送者是自己
            communication = this.getMyselfCommunication(senderId);
            return ResponseResult.SUCCESS(communication);
        }

        // 查询好友，群聊会话
        communication = this.chatCommunicationMapper.selectCommunication(senderId, receiverId);
        if (Objects.isNull(communication)) {
            // 会话不存在
            return ResponseResult.FAIL(communication);
        }

        return ResponseResult.SUCCESS(communication);
    }


    /**
     * 获取用户会话列表
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getUserCommunicationList(Long userId) {

        // 获取原始会话数据
        List<ChatCommunication> communicationList = this.lambdaQuery()
                .eq(ChatCommunication::getSenderId, userId)
                .eq(ChatCommunication::getDeleted, Boolean.FALSE)
                // TODO 此处已经修改为双向会话关系
                /*.or(wrapper -> {
                    wrapper.eq(ChatCommunication::getReceiverId, userId);
                })*/
                .orderByDesc(ChatCommunication::getUpdateTime)
                .list();
        if (CollectionUtils.isEmpty(communicationList)) {
            communicationList = Collections.emptyList();
        }

        // 组装 friend和group 信息
        List<ChatCommunicationVO> voList = communicationList.stream().map(talk -> {
            // 数据容器
            FriendVO friend = null;
            GroupVO group = null;
            Integer sessionType = talk.getSessionType();
            if (sessionType == CommunicationConstants.SessionType.PRIVATE.ordinal()) {
                // 好友私聊
                friend = this.friendService.getUserFriendById(userId, talk.getReceiverId());
            } else if (sessionType == CommunicationConstants.SessionType.GROUP.ordinal()) {
                // 群聊
                group = this.groupService.getGroupVOById(talk.getReceiverId());
            }
            // 组合
            return CommunicationMapStructure.INSTANCE.toVO(talk, friend, group);
        }).collect(Collectors.toList());

        // 设置其余属性
        voList.forEach(vo -> {
            Integer sessionType = vo.getSessionType();
            if (sessionType == CommunicationConstants.SessionType.PRIVATE.ordinal()) {
                // 好友
                vo.setReceiverName(StringUtils.isEmpty(vo.getFriendVO().getRemarkName())
                        ? vo.getFriendVO().getNickname() : vo.getFriendVO().getRemarkName());
                // 头像
                vo.setAvatar(vo.getFriendVO().getAvatar());
                // 在线状态
                vo.setOnline(this.authorizationService.getUserOnlineStatus(vo.getReceiverId()));
            } else if (sessionType == CommunicationConstants.SessionType.GROUP.ordinal()) {
                // 群聊
                vo.setReceiverName(vo.getGroupVO().getGroupName());
                // 头像
                vo.setAvatar(vo.getGroupVO().getGroupAvatar());
            }
        });


        // TODO 排序: 置顶->跟新时间->免打扰->名称
        Comparator<ChatCommunicationVO> comparator = Comparator.comparing(ChatCommunicationVO::getTop).reversed()
                .thenComparing(ChatCommunicationVO::getUpdateTime).reversed()
                .thenComparing(ChatCommunicationVO::getDisturb)
                .thenComparing(ChatCommunicationVO::getUnreadCount).reversed()
                .thenComparing(ChatCommunicationVO::getOnline).reversed();
        // 进行排序
        voList = voList.stream().sorted(comparator).collect(Collectors.toList());

        // 响应数据
        return ResponseResult.SUCCESS(voList);
    }


    /**
     * 跟新会话草稿内容
     * @param messageBO
     * @return
     */
    @Override
    public ResponseResult updateCommunicationDraft(MessageBO messageBO) {

        // 获取素材
        Long senderId = messageBO.getSenderId();
        Long receiverId = messageBO.getReceiverId();

        // 查询会话
        ChatCommunication communication = this.lambdaQuery()
                .eq(ChatCommunication::getSenderId, senderId)
                .eq(ChatCommunication::getReceiverId, receiverId)
                .one();

        // 草稿，时间
        communication.setDraft(messageBO.getContent())
                .setUpdateTime(LocalDateTime.now());

        // 更新
        boolean update = this.updateById(communication);
        if (BooleanUtils.isFalse(update)) {
            // 更新失败
            return ResponseResult.FAIL();
        }
        return ResponseResult.SUCCESS(communication);
    }


    /**
     * 置顶、取消置顶会话
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult topTalkCommunication(TopTalkRequest request) {

        // 参数校验
        boolean preChek = Objects.isNull(request) || StringUtils.isEmpty(request.getTalkId());
        if (BooleanUtils.isTrue(preChek)) {
            // 校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        String talkId = request.getTalkId();
        // 获取会话
        ChatCommunication talk = this.getById(talkId);

        if (Objects.isNull(talk)) {
            // 会话不存在
            return ResponseResult.FAIL().setMessage("会话不存在!");
        }
        Friend friend = this.friendService.getFriendById(talk.getSenderId(), talk.getReceiverId());

        // 根据操作进行处理
        Integer operation = request.getOperation();

        if (operation == TopTalkRequest.Operation.TOP.ordinal()) {
            // 置顶 :1.检查是否置顶 2.置顶
            if (BooleanUtils.isTrue(talk.getTop())) {
                // 已经置顶了
                return ResponseResult.SUCCESS(talk);
            }
            // 没有置顶 -> 进行置顶操作
            talk.setTop(Boolean.TRUE);
            friend.setTop(Boolean.TRUE);

        } else if (operation == TopTalkRequest.Operation.CANCEL.ordinal()) {
            // 取消置顶：1.检查是否已经置顶，2.进行取消操作
            if (BooleanUtils.isFalse(talk.getTop())) {
                // 已经不是置顶了
                return ResponseResult.SUCCESS(talk);
            }
            // 进行取消置顶操作
            talk.setTop(Boolean.FALSE);
            friend.setTop(Boolean.FALSE);
        }

        // 好友置顶
        this.updateById(talk);
        this.friendService.updateById(friend);

        // 响应操作后的数据
        return ResponseResult.SUCCESS(talk);
    }


    /**
     * 清空用户未读消息： 清空 senderId=userId, receiverId=好友id 的会话未读数量
     * @param request
     * @return
     */
    @Override
    public ResponseResult clearUnreadMessage(ClearUnreadRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getReceiverId()) || Objects.isNull(request.getTalkType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 素材
        Long receiverId = Long.valueOf(request.getReceiverId());
        Integer talkType = request.getTalkType();
        Long senderId = Long.valueOf(request.getSenderId());
        if (Objects.isNull(senderId)) {
            senderId = UserContextHolder.get().getId();
        }

        // 查询会话
        ChatCommunication talk = this.chatCommunicationMapper.selectCommunication(senderId, receiverId);
        if (Objects.isNull(talk)) {
            return ResponseResult.FAIL();
        }

        // 清空会话未读消息
        talk.setUnreadCount(0);
        boolean update = this.updateById(talk);

        // 响应
        return ResponseResult.SUCCESS(update).setMessage("清空未读消息数成功!");
    }


    /**
     * 修改会话未读数量
     * @param talk
     * @param increase
     * @return
     */
    @Override
    public ResponseResult increaseTalkUnreadCount(ChatCommunication talk, int increase) {

        Boolean res = this.chatCommunicationMapper.increaseTalkUnreadCount(talk.getId(), increase);

        return new ResponseResult().setSuccess(res);
    }


    /**
     * 获取自己的会话
     * @param userId 当前用户自己
     * @return
     */
    @Override
    public ChatCommunication getMyselfCommunication(Long userId) {

        ChatCommunication talk = this.lambdaQuery()
                .eq(ChatCommunication::getSenderId, userId)
                .eq(ChatCommunication::getReceiverId, userId)
                .orderByAsc(ChatCommunication::getId)
                .last(" limit 1")
                .one();

        return talk;
    }


    /**
     * 移除会话
     * @param request
     * @return
     */
    @Override
    public ResponseResult removeUserTalk(RemoveTalkRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getTalkId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL(ResultCode.BAD_REQUEST);
        }

        // 素材
        Long talkId = request.getTalkId();
        Long userId = request.getUserId();
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }

        // 查询会话
        ChatCommunication talk = this.getById(talkId);
        if (Objects.isNull(talk)) {
            // 会话已经删除了
            return ResponseResult.SUCCESS();
        }

        // 是否具有删除权限
        boolean equals = Objects.equals(userId, talk.getBelongUserId());
        if (BooleanUtils.isFalse(equals)) {
            // 会话不是本人的
            return ResponseResult.FAIL();
        }

        // 进行逻辑删除会话
        talk.setDeleted(Boolean.TRUE);
        boolean update = this.updateById(talk);

        if (BooleanUtils.isFalse(update)) {
            // 移除会话失败
            return ResponseResult.FAIL();
        }

        // 移除会话成功
        return ResponseResult.SUCCESS();
    }


    /**
     * 开启或关闭消息免打扰
     * @param request
     * @return
     */
    @Override
    public ResponseResult setTalkDisturb(SetTalkDisturbRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getReceiverId()) || Objects.isNull(request.getTalkId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 素材
        Long userId = request.getUserId();
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }
        Long talkId = request.getTalkId();
        Long receiverId = request.getReceiverId();

        // 查询会话是否存在
        ChatCommunication talk = this.getById(talkId);
        if (Objects.isNull(talk)) {
            // 通过会话id获取会话失败 -> 尝试通过会话属性获取
            talk = this.existsChatCommunication(userId, receiverId);
        }

        if (Objects.isNull(talk)) {
            // 会话不存在
            return ResponseResult.FAIL().setMessage("会话不存在!");
        }

        // 会话存在，比对是否会话属主
        if (!Objects.equals(userId, talk.getBelongUserId())) {
            // 会话不属于当前用户
            return ResponseResult.FAIL().setMessage("当前会话不属于您!");
        }

        // 会话属于您，修改是否打扰
        talk.setDisturb(request.getDisturb());
        boolean update = this.updateById(talk);

        if (BooleanUtils.isFalse(update)) {
            // 修改打扰状态失败
            return ResponseResult.FAIL();
        }

        return ResponseResult.SUCCESS();
    }
}




