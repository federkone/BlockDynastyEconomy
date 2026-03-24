package com.blockdynasty.economy.configFromChannel;

import BlockDynasty.Economy.domain.services.courier.Message;

public class SyncDataMessage extends Message<SyncDataMessage> {
    public static SyncDataMessage.ExtendedMessageBuilder builder() {
        return new SyncDataMessage.ExtendedMessageBuilder();
    }
    public static class ExtendedMessageBuilder extends Builder<SyncDataMessage> {
        public ExtendedMessageBuilder() {
            super(SyncDataMessage.class);
        }
    }
}