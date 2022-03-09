package com.sanri.test.netty.protocol;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.junit.Test;

@Getter
@ToString
public class LoginMessage {
    private String imei;
    private byte version;

    private LoginMessage(){}

    /**
     * 解析登录消息
     * @param content
     * @return
     */
    public static LoginMessage parse(byte [] content){
        LoginMessage loginMessage = new LoginMessage();
        byte [] imei = new byte[8];
        System.arraycopy(content,0,imei,0,8);
        loginMessage.imei = bcdToString(imei);
        loginMessage.version = content[8];
        return loginMessage;
    }

    /**
     * 将BCD码转成String
     *
     * @param b
     * @return
     */
    public static String bcdToString(byte [] b){
        StringBuffer sb = new StringBuffer ();
        for (int i = 0;i < b.length;i++ ){
            int h = ((b [i] & 0xff) >> 4) + 48;
            sb.append ((char) h);
            int l = (b [i] & 0x0f) + 48;
            sb.append ((char) l);
        }
        return sb.toString () ;
    }

}
