package com.sanri.test.netty.protocol;

import lombok.Data;

@Data
public class MessageProtocol {
    private short start = PROTOCOL_START;
    private byte length = 1; // 默认长度为 1 , 即协议号长度
    private byte command;
    private byte[] payload;
    private short end = PROTOCOL_END;

    public static short PROTOCOL_START = 0x7878;
    public static short PROTOCOL_END = 0x0D0A;

    private MessageProtocol(){}

    /**
     * 创建一条消息
     * @param command
     * @param payload
     * @return
     */
    public static MessageProtocol createMessage(byte command,byte [] payload){
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.command = command;
        messageProtocol.payload = payload;
        if (messageProtocol.payload != null) {
            messageProtocol.length = (byte) (payload.length + 1);
        }
        return messageProtocol;
    }

    public static MessageProtocol createMessage(byte command){
        return createMessage(command,null);
    }
}
