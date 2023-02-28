package com.hk.im.server.common.message;

public class PingMessage extends AbstractMessage {
    @Override
    public int getMessageType() {
        return MessageTypeDeterminer.PingMessageType;
    }
}
