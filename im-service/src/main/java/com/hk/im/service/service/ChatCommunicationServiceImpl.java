package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.ChatCommunicationService;
import com.hk.im.client.service.FriendService;
import com.hk.im.client.service.GroupService;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.request.CreateCommunicationRequest;
import com.hk.im.domain.request.TopTalkRequest;
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
        boolean talkResult = this.existsChatCommunication(senderId,
                receiverId);
        if (BooleanUtils.isTrue(talkResult)) {
            // 会话已经存在
            return ResponseResult.SUCCESS("会话已经存在了!");
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
            boolean exists = this.existsChatCommunication(receiverId, senderId);
            if (BooleanUtils.isFalse(exists)) {
                // 会话不存在，创建会话
                ChatCommunication talkTwo = new ChatCommunication()
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
        if (sessionType == CommunicationConstants.SessionType.GROUP) {
            name = request.getReceiverId();
        }
        String key = RedisConstants.COMMUNICATION_KEY + RedisConstants.SEQUENCE_KEY + name;

        // 设置到 redis 缓存
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
    public boolean existsChatCommunication(Long senderId, Long receiverId) {
        // 判断是否存在
        boolean exists = this.lambdaQuery()
                .eq(ChatCommunication::getSenderId, senderId)
                .eq(ChatCommunication::getReceiverId, receiverId)
                .exists();

        return exists;
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

        ChatCommunication communication = this.chatCommunicationMapper.selectCommunication(senderId, receiverId);
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
        }).toList();

        // 设置其余属性
        voList.forEach(vo -> {
            Integer sessionType = vo.getSessionType();
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
        });


        // TODO 排序: 置顶->跟新时间->免打扰->名称
        Comparator<ChatCommunicationVO> comparator = Comparator.comparing(ChatCommunicationVO::getTop).reversed()
                .thenComparing(ChatCommunicationVO::getUpdateTime).reversed()
                .thenComparing(ChatCommunicationVO::getDisturb)
                .thenComparing(ChatCommunicationVO::getUnreadCount).reversed()
                .thenComparing(ChatCommunicationVO::getOnline).reversed();
        // 进行排序
        voList = voList.stream().sorted(comparator).toList();

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
}




