package learntest.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import learntest.rabbitmq.RabbitmqUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SenderWork {
    private static final String  QUEUE_NAME = "test_work";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitmqUtil.connection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for (int i = 0; i <50; i++) {
            String sendmsg = "["+i+"]"+RandomStringUtils.randomAlphanumeric(50);
            channel.basicPublish("",QUEUE_NAME,null, sendmsg.getBytes());
            System.out.println("send msg["+i+"]:"+sendmsg);
            Thread.sleep(100);
        }

        channel.close();
        connection.close();
    }
}
