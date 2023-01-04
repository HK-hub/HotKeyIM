######################## Hot Key IM
# 用户表
create table tb_user (
                         id long primary key comment '用户id',
                         username varchar(32) comment '用户名，昵称',
                         account varchar(16) unique comment '账号，类比QQ号,唯一性',
                         password varchar(128) comment '加密后的密码,盐值为原密码',
                         phone varchar(11) unique comment '大陆手机号,唯一性，一个手机只能绑定一个账号',
                         email varchar(32) unique comment '邮箱,唯一性,一个邮箱只能绑定一个账号',
                         big_avatar varchar(255)  comment '用户大头像',
                         mini_avatar varchar(255) comment '用户头像缩略图',
                         qrcode varchar(255) comment '用户二维码',
                         cid varchar(64) comment '会话唯一标识',
                         create_time datetime comment '创建时间',
                         update_time datetime comment '更新时间'
);

# 用户信息表
create table tb_user_info (
                              # 账户体系
                                  user_id long primary key comment '用户id',
                              nickname varchar(32) comment '用户昵称，同用户名',
                              qq varchar(16) comment '绑定的QQ号',
                              wechat varchar(32) comment '绑定的微信号',
                              github varchar(32) comment 'github 账号',
                              dingtalk varchar(32) comment '钉钉账号',
                              openid varchar(255) comment 'openid 可用于微信登录',
                              wallet decimal default 0 comment '用户钱包剩余余额',
                              `last_time` datetime COMMENT '最后交互时间',
                              status int comment '用户状态：1.离线，2.在线，3.隐身,4.挂起，5.忙碌',

                              # 资料体系
                                  gender boolean comment '性别: 1.男，0.女',
                              age int comment '年龄',
                              birthday date comment '生日',
                              constellation varchar(16) comment '星座',
                              campus varchar(32) comment '学校',
                              major varchar(32) comment '专业,主修',
                              job varchar(32) comment '职业',
                              city varchar(32) comment '所在城市',
                              interest varchar(128) comment '兴趣爱好',
                              tag varchar(128) comment '个人标签,不超过6个标签，每个标签不超过6个字',
                              signature varchar(255) comment '个性签名，类比QQ签名',

                              # 配置体系
                                  add_friend_policy int default 1 comment '加好友策略：0.直接同意，1.验证，2.回答问题，3.输入密码',
                              add_friend_question varchar(32) comment '加好友问题',
                              add_friend_answer varchar(32) comment '加好友答案，密码',

                              create_time datetime comment '创建时间',
                              update_time datetime comment '更新时间'

);

# 好友表
create table tb_friend (
                           id long primary key comment '好友关系id',
                           user_id long comment '用户id',
                           friend_id long comment '好友id',
                           relation int comment '状态：0.陌生人(临时会话)，1.好友，2.黑名单，3.特别关心，4.删除',
                           `group` varchar(32) comment '分组:如果不是好友，默认临时会话',
                           remark_name varchar(32) comment '备注姓名',
                           remark_info varchar(255) comment '备注信息',
                           create_time datetime comment '创建时间',
                           update_time datetime comment '更新时间'
);

# 好友/群申请表
create table tb_friend_apply (
                                 id long primary key comment 'id编号',
                                 sender_id long comment '申请发起人',
                                 acceptor_id long comment '申请接收人,如果是加群则为群号',
                                 apply_type int comment '申请类型：1.好友申请，2.加群申请',
                                 apply_info varchar(255) comment '好友申请说明信息,验证信息',
                                 status int comment '状态:1.待处理，2.同意，3.拒绝',
                                 create_time datetime comment '创建时间',
                                 update_time datetime comment '更新时间'
);

# 群组表
create table tb_group (
                          id long primary key comment '群id',
                          group_account long unique comment '群账号',
                          group_name varchar(64) comment '群聊名称',
                          group_avatar varchar(255) comment '群聊头像',
                          description varchar(255) comment '群描述',
                          group_type int default 0 comment '群类型:0.未知，1.兴趣爱好，2.行业交流，3.生活休闲，3.学习考试，4.娱乐游戏，5.置业安家，6.品牌产品，7.粉丝，8.同学同事，9.家校师生',
                          qrcode varchar(255) comment '群二维码',
                          member_count int comment '群人数',
                          group_master long comment '群主',
                          create_time datetime comment '创建时间',
                          update_time datetime comment '更新时间'
);

# 群设置表: 部分特殊类型的群的附加信息
create table tb_group_setting (
                                  group_id long primary key comment '群id',
                                  group_account long unique comment '群号',
                                  position varchar(128) comment '群定位:国家-省份-城市-区-县-镇',
                                  member_capacity int default 200 comment '群人数限制:200人，500人，1000人，2000人',
                                  find_type int comment '发现群方式：1.公开群(支持搜索群名称，群号，群二维码，邀请)，2.不公开群(不支持搜索群名称，支持搜索群号，群二维码，邀请)，3.邀请制(只能通过成员邀请)',
                                  join_type int comment '加群方式：1.允许任何人everybody,2.需要验证verification, 3.不允许人加群nobody',
                                  forbid_send boolean comment '全员禁言',
                                  announcement varchar(255) comment '群最新公告',
                                  create_time datetime comment '创建时间',
                                  update_time datetime comment '更新时间'
)default charset = utf8mb4;

# 群公告表
create table tb_group_announcement (
                                       id long primary key comment '公告id',
                                       group_id long comment '群id',
                                       group_account long unique comment '群号',
                                       type int comment '群公告类型：1.普通公告，2.置顶公告, 3.新人公告',
                                       author long comment '公告撰写者',
                                       title varchar(32) comment '公告标题',
                                       content varchar(255) comment '公告内容',
                                       deleted boolean comment '是否删除',
                                       create_time datetime comment '创建时间',
                                       update_time datetime comment '更新时间'
);
create index 'idx_gn_gid' on tb_group_announcement(group_id);

# 群成员表
create table tb_group_member (
                                 id long primary key comment 'id',
                                 group_id long comment '群id',
                                 group_account long comment '群号',
                                 member_id long comment '群成员id',
                                 member_username varchar(32) comment '群成员群外昵称',
                                 member_remark_name varchar(32) comment '群成员的群内昵称',
                                 member_avatar varchar(255) comment '群成员头像(缩略图)',
                                 member_role int default 1 comment '群员角色:1.普通成员，2.管理员，3.群主',
                                 group_category varchar(32) comment '群分组，群分类',
                                 gag_time datetime comment '禁言时间：表示禁止发言的结束时间',
                                 status int comment '群状态：1.加群，2.退群，3.群黑名单(前提是已经被踢出群聊)',
                                 create_time datetime comment '创建时间',
                                 update_time datetime comment '更新时间'
);


# 群申请
-- auto-generated definition
create table tb_group_apply
(
    id          bigint        not null comment 'id编号'
        primary key,
    sender_id   bigint        null comment '申请发起人',
    group_id bigint        null comment '申请群号',
    handler_id bigint comment '处理人id',
    apply_type  int           null comment '申请类型：1.搜索加群，2.邀请入群，3.扫码加群',
    apply_info  varchar(255)  null comment '申请说明信息,验证信息',
    status      int           null comment '状态:1.待处理，2.同意，3.拒绝',
    sign        int default 0 null comment '签收状态：0.未签收，1.已签收',
    create_time datetime      null comment '创建时间',
    update_time datetime      null comment '更新时间'
);



# 聊天消息表
create table tb_chat_message (
                                 id long primary key comment '聊天消息表id',
                                 message_feature int comment '消息属性：0.默认，1.离线消息，2.漫游消息，3.同步消息，4.透传消息，5.控制消息',
                                 message_type int comment '消息类型:1.文本，2.图片，3.语音，4.图文混合，5.文件，6.语音通话，7.视频通话，
                                8.白板演示，9.远程控制，10.日程安排，11.外部分享,12.@消息，13.红包消息',
                                 # 后续为消息添加前缀索引
                                     content text(2048) comment '消息内容,最大文本数量1024个字符',
                                 extra JSON comment '扩展字段，一般使用JSON字符串存储,可以用户回复消息，@消息，超文本消息，卡片消息，视频消息等',
                                 deleted boolean comment '是否删除该条聊天记录,0.false, 1.ture',
                                 create_time datetime comment '创建时间',
                                 update_time datetime comment '更新时间'
)default charset=utf8mb4;


## 消息流水表：解决群聊全部群员都会收到相同的消息问题，如果只用单个表存储消息接收情况，会存在大量荣誉消息：
## https://developer.aliyun.com/article/138361
create table tb_message_flow (
                                 id long primary key comment '聊天消息表id',
                                 group_id long comment '群id,如果是群聊的话',
                                 sender_id long comment '消息发送者id',
                                 acceptor_id long comment '消息接收者id(用户id或群id)',
                                 chat_type int comment '会话类型:1.个人聊天,2.群聊，3.系统消息,',
                                 message_type int comment '消息类型:1.文本，2.图片，3.语音，4.图文混合，5.文件，6.语音通话，7.视频通话，
                                8.白板演示，9.远程控制，10.日程安排，11.外部分享,12.@消息',
                                 # 后续为消息添加前缀索引
                                     message_id long comment '聊天消息id',
                                 send_status int comment '消息发送状态：1.发送中，2.已发送，3.发送失败,4.草稿，',
                                 sign_flag int comment '签收状态：1.未读，2.已读，3.忽略，4.撤回，5.删除',
                                 deleted boolean comment '是否删除该条聊天记录,0.false, 1.ture',
                                 create_time datetime comment '创建时间',
                                 update_time datetime comment '更新时间'
);

# 会话表/回话表
create table tb_chat_communication (
                                       id long primary key comment '会话表id',
                                       --# 该条消息所属消息，比如我登陆了，我发送/接收到消息入库的时候写入自己的uid，他的作用是多用户登陆的时候区分回话表
                                           belong_user_id long comment '消息属主',
                                       --# 服务器生产回话id,当前的回话id，它作用是标识一个回话，比如我跟你聊天or你跟我聊天，我们的回话id应该是一致的，对于群聊也是，在群中发送消息，每个人的回话id是一致的。
                                           session_id varchar(64) comment '回话id',
                                       sender_id long comment '发送者id',
                                       acceptor_id long comment '接收者id',
                                       group_id long comment '群聊id,用于扩展群内@消息',
                                       --# 最后一条消息的id，作用是用于比较，比如在聊天页面中，删除了一条消息，撤回了一条消息之类的，这时候可以根据这个msg_id进行删除修改和更新。
                                           --# chat_name -> varchar 聊天者的名称，比如我与你聊天，我的表中的这个字段就是你的name,你的表中就是我的name.
                                       last_message_id long comment '最后一条消息id',
                                       last_message_content text(2048) comment '最后一条消息的内容',
                                       last_sender_username varchar(32) comment '最后的消息发送者名称',
                                       last_send_time datetime comment '最后消息发送时间',
                                       session_type int comment '回话类型(1.个人聊天，2.群聊消息，3.系统消息,4.控制消息)',
                                       session_status int(11) DEFAULT NULL COMMENT '会话修改命令（预留）',
                                       unread_count int comment '未读消息数量',

                                       create_time datetime comment '创建时间',
                                       update_time datetime comment '更新时间'


);