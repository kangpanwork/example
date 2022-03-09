package com.sanri.test.rocketmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.common.admin.TopicOffset;
import org.apache.rocketmq.common.admin.TopicStatsTable;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.srvutil.ServerUtil;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExtImpl;
import org.apache.rocketmq.tools.admin.MQAdminExt;
import org.apache.rocketmq.tools.command.SubCommandException;
import org.apache.rocketmq.tools.command.topic.UpdateTopicSubCommand;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.*;

public class NoSpringMain {

    /**
     * 需要手动指定存储到哪些 broker 上
     */
    @Test
    public void createTopic() throws SubCommandException {
        String topicName = "unit-test-from-java-1";
        String[] subargs = new String[] {
                "-b 192.168.108.130:10911",
                "-t "+topicName,
                "-r 8",
                "-w 8",
                "-p 6",
                "-o false",
                "-u false",
                "-s false"};

        UpdateTopicSubCommand cmd = new UpdateTopicSubCommand();
        Options options = ServerUtil.buildCommandlineOptions(new Options());
        final Options updateTopicOptions = cmd.buildCommandlineOptions(options);
        final CommandLine commandLine = ServerUtil
                .parseCmdLine("mqadmin ",subargs, updateTopicOptions, new PosixParser());

        cmd.execute(commandLine, updateTopicOptions, null);
    }

    /**
     * 推消息 , 有消息才推 ;使用的长轮询
     * @throws MQClientException
     * @throws InterruptedException
     */
    @Test
    public void testConsumer2() throws MQClientException, InterruptedException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("console1");
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.subscribe("TopicTest","*");
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt messageExt : list) {
                    if (messageExt.getReconsumeTimes() == 3){
                        //可以将对应的数据保存到数据库，以便人工干预
                        System.out.println(messageExt.getMsgId()+","+messageExt.getBody());
                        System.out.println("消费到的消息:"+messageExt);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                }
                //返回消费状态
                //CONSUME_SUCCESS 消费成功
                //RECONSUME_LATER 消费失败，需要稍后重新消费
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });

        //调用start()方法启动consumer
        defaultMQPushConsumer.start();
        System.out.println("Consumer Started.");

        Thread.currentThread().join();
    }

    /**
     * 拉消息 , 对于初次使用还是要方便一点
     * @throws MQClientException
     */
    @Test
    public void testConsumer() throws MQClientException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("please_rename_unique_group_name_5");
        consumer.setNamesrvAddr("192.168.108.130:9876");
        consumer.setInstanceName("consumer");
        consumer.start();

        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("TopicTest");
        for (MessageQueue mq : mqs) {
            System.out.printf("Consume from the queue: %s%n", mq);
            SINGLE_MQ:
            while (true) {
                try {
                    PullResult pullResult = consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                    System.out.printf("%s%n", pullResult);
                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                    switch (pullResult.getPullStatus()) {
                        case FOUND:
                            System.out.println(pullResult.getMsgFoundList().get(0).toString());
                            break;
                        case NO_NEW_MSG:
                            break SINGLE_MQ;
                        case NO_MATCHED_MSG:
                        case OFFSET_ILLEGAL:
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    //TODO
                }
            }
        }
        consumer.shutdown();
    }

    private static final Map<MessageQueue, Long> offseTable = new HashMap<MessageQueue, Long>();

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offseTable.get(mq);
        if (offset != null) {
            return offset;
        }
        return 0;

    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        offseTable.put(mq, offset);
    }

    @Test
    public void testProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");

        producer.setNamesrvAddr("192.168.108.130:9876");
        producer.start();

        for (int i = 0; i < 10; i++)
            try {
                Message msg = new Message("TopicTest",
                        "TagA",
                        "OrderID188",
                        "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult send = producer.send(msg);
                System.out.printf("%s%n", send);

            } catch (Exception e) {
                e.printStackTrace();
            }

        producer.shutdown();
    }

    /**
     * 严格顺序消息
     * @throws MQClientException
     * @throws UnsupportedEncodingException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQBrokerException
     */
    @Test
    public void testSequenceMessage() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup1");
//        producer.setNamesrvAddr("192.168.229.5:9876;192.168.229.6:9876");
        producer.setNamesrvAddr("192.168.108.130:9876");
        producer.setRetryTimesWhenSendFailed(3);
        producer.start();
        String[] tags = new String[]{"创建订单", "支付", "发货", "收货", "五星好评"};
        for (int i = 5; i < 25; i++) {
            int orderId = i / 5;
            Message msg = new Message("mm-topic", tags[i % tags.length], "uniqueId:" + i,
                    ("order_" + orderId + " " + tags[i % tags.length]).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    //此刻arg == orderId,可以保证是每个订单进入同一个队列
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);
            System.out.printf("%s%n", sendResult);
        }
        producer.shutdown();
    }

    /**
     * 订阅
     */
    @Test
    public void consumer1() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order_Consumer");
        consumer.setNamesrvAddr("192.168.108.130:9876");
        try {
            //设置Consumer从哪开始消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.subscribe("mm-topic", "*");
            // 实现了MessageListenerOrderly表示一个队列只会被一个线程取到, 第二个线程无法访问这个队列,MessageListenerOrderly默认单线程
//            consumer.setConsumeThreadMin(3);
//            consumer.setConsumeThreadMax(6);
            consumer.registerMessageListener(new MessageListenerOrderly() {
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    try {
                        System.out.println("orderInfo: " + new String(msgs.get(0).getBody(), "utf-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Consumer1 Started.");
        while (true) {
        }
    }

    /**
     * 事务消息
     */
    @Test
    public void transMessage() throws MQClientException, InterruptedException {
        TransactionListener transactionListener = new TransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("please_rename_unique_group_name");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();
        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            try {
                Message msg =
                        new Message("TopicTest1234", tags[i % tags.length], "KEY" + i,
                                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.sendMessageInTransaction(msg, null);
                System.out.printf("%s%n", sendResult);
                Thread.sleep(10);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
        }
        producer.shutdown();
    }

    /**
     * admin mq
     */
    @Test
    public void topics() throws RemotingException, MQClientException, InterruptedException {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt("console");
        defaultMQAdminExt.setNamesrvAddr("192.168.108.130:9876");
        MQAdminExt mqAdminExt = new DefaultMQAdminExtImpl(defaultMQAdminExt,6000);
        mqAdminExt.start();

        TopicList topicListObj = mqAdminExt.fetchAllTopicList();
        Set<String> topicList = topicListObj.getTopicList();
        System.out.println(topicList);

        String brokerAddr = topicListObj.getBrokerAddr();       // null ???
        System.out.println(brokerAddr);
    }

    @Test
    public void queueOffset() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt("console");
        defaultMQAdminExt.setNamesrvAddr("192.168.108.130:9876");
        MQAdminExt mqAdminExt = new DefaultMQAdminExtImpl(defaultMQAdminExt,6000);
        mqAdminExt.start();

        TopicStatsTable topicStatsTable = mqAdminExt.examineTopicStats("TopicTest");
        HashMap<MessageQueue, TopicOffset> offsetTable = topicStatsTable.getOffsetTable();
        Iterator<Map.Entry<MessageQueue, TopicOffset>> iterator = offsetTable.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<MessageQueue, TopicOffset> next = iterator.next();
            TopicOffset value = next.getValue();
            MessageQueue key = next.getKey();
            System.out.println(key+"- " + value.getMinOffset() + ":"+value.getMaxOffset()+":"+value.getLastUpdateTimestamp());
        }
    }

    @Test
    public void testMain() throws MQClientException, InterruptedException, MQBrokerException, RemotingException {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt("console");
        defaultMQAdminExt.setNamesrvAddr("192.168.108.130:9876");
        MQAdminExt mqAdminExt = new DefaultMQAdminExtImpl(defaultMQAdminExt,6000);
        mqAdminExt.start();

        Set<String> topicTest = mqAdminExt.getTopicClusterList("TopicTest");
        System.out.println(topicTest);

        ClusterInfo clusterInfo = mqAdminExt.examineBrokerClusterInfo();
        HashMap<String, BrokerData> brokerAddrTable = clusterInfo.getBrokerAddrTable();
        Iterator<Map.Entry<String, BrokerData>> iterator = brokerAddrTable.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, BrokerData> entry = iterator.next();
            String key = entry.getKey();
            BrokerData value = entry.getValue();
            System.out.println(key+"-"+value);
        }

    }
}
