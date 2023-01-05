# 一个较为完善的IM及时通讯应用的消息设计


在综合考虑对比了`轮询`，`长轮询`，`SSE+UDP/QUIC`，`WebSocket` 等一系列的用于维持聊天应用通信的方案后，最终选择了基于 `WebSocket` 的通信方式来作为我的IM应用的聊天传输协议

相对与其他而言，它具有一下比较明显的优势：
1. 全双工
2. 使用较为简单，实现复杂度低
3. 在大量连接下，性能效率更好
4. 技术支持较为完善，相关资料丰富


## 消息
## 聊天消息
用于传输，聊天中产生的各种超媒体消息数据：


| 名称        | 类名                       | 传输协议      |
|-----------|--------------------------|-----------|
| 连接初始化消息   | ConnectMessage           | WebSocket |
| 普通聊天消息    | SimpleChatMessage        | WebSocket |
| 图片聊天消息    | PictureChatMessage       | HTTP      |
| 文件/视频聊天消息 | MultipartFileChatMessage | HTTP      |
| 消息签收消息    | MessageSignedMessage     | WebSocket |
| 好友拉取消息    | FriendPullMessage        | WebSocket |
| 好友拉取消息    | FriendPullMessage        | WebSocket |




## 控制消息
## 系统消息

## text文本类型消息




## 自定义协议/二进制数据消息