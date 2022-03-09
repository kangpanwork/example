package com.sanri.test.testspringbootkafka.proxy;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
@Component
public class QueueInvocationHandler implements InvocationHandler {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ListableBeanFactory beanFactory;
    @Autowired
    private TopicListenDispatch topicListenDispatch;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            log.info("当前是 Object 中的方法调用 "+method);
            return method.invoke(this,args);
        }
        Class<?> returnType = method.getReturnType();
        Class<?>[] parameterTypes = method.getParameterTypes();

        Class<?> declaringClass = method.getDeclaringClass();
        QueueClient queueClient = AnnotationUtils.findAnnotation(declaringClass, QueueClient.class);
        QueueRequest queueRequest = AnnotationUtils.getAnnotation(method, QueueRequest.class);
        String requestTopic = queueRequest.request();

//        ParameterizedType parameterizedType = (ParameterizedType)returnType.getGenericInterfaces()[0];
//        Class actualTypeArgument = (Class) parameterizedType.getActualTypeArguments()[0];

        // 取 args 第一个参数
        if(ArrayUtils.isNotEmpty(args)){
            Object arg = args[0];
            String reqData = JSON.toJSONString(arg);

            // 发起监听
            String match = queueRequest.match();
            MessageMatch messageMatch = beanFactory.getBean(match, MessageMatch.class);
            MatchListenableFuture matchListenableFuture = new MatchListenableFuture(requestTopic,reqData,messageMatch);
            ListenableFuture listenableFuture = topicListenDispatch.addEvent(matchListenableFuture);

            kafkaTemplate.send(requestTopic, reqData);

            return listenableFuture;
        }

        return null;
    }
}
