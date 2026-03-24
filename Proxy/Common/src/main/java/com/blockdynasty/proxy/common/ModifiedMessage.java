/**
 * Copyright 2026 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blockdynasty.proxy.common;

public class ModifiedMessage implements Message{
    private Message originalMessage;
    private byte[] modifiedData;

    public  ModifiedMessage(Message originalMessage, byte[] modifiedData) {
        this.originalMessage = originalMessage;
        this.modifiedData = modifiedData;
    }

    @Override
    public String getChannelName() {
        return originalMessage.getChannelName();
    }

    @Override
    public boolean isServerMessage() {
        return originalMessage.isServerMessage();
    }

    @Override
    public byte[] getData() {
        return modifiedData;
    }

    @Override
    public String getServerSourceName() {
        return originalMessage.getServerSourceName();
    }

    @Override
    public void markAsHandled() {
        originalMessage.markAsHandled();
    }
}
