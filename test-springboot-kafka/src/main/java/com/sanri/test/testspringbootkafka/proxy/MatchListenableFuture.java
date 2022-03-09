package com.sanri.test.testspringbootkafka.proxy;

import lombok.Getter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.ArrayBlockingQueue;

@Getter
public class MatchListenableFuture {
    private String topic;
    private MessageMatch messageMatch;
    private ListenableFutureTask listenableFutureTask;
    private String requestData;
    private String data;

    public MatchListenableFuture(String topic,String requestData,MessageMatch messageMatch) {
        this.requestData = requestData;
        this.topic = topic;
        this.messageMatch = messageMatch;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setListenableFutureTask(ListenableFutureTask listenableFutureTask) {
        this.listenableFutureTask = listenableFutureTask;
    }
}
