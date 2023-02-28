package com.hk.im.server.common.message;

public class PongMessage extends AbstractMessage {
    @Override
    public int getMessageType() {
        return MessageTypeDeterminer.PongMessageType;
    }
}
