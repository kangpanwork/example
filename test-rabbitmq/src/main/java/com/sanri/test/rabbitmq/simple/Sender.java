package learntest.rabbitmq.simple;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import learntest.rabbitmq.RabbitmqUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Sender {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitmqUtil.connection();

        Channel channel = connection.createChannel();
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-max-length",10);
        channel.queueDeclare("test",false,false,false,arguments);
        for (int i = 0; i <1000; i++) {
            String sendmsg = RandomStringUtils.randomAlphanumeric(20);
            // 消息持久化
            AMQP.BasicProperties basicProperties = MessageProperties.PERSISTENT_TEXT_PLAIN.builder().build();
            channel.basicPublish("","test",basicProperties, sendmsg.getBytes());
            System.out.println("send msg["+i+"]:"+sendmsg);
        }


        channel.close();
        connection.close();
    }
}
