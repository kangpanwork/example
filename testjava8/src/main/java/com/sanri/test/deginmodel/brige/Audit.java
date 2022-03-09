package com.sanri.test.deginmodel.brige;

import java.util.UUID;

public class Audit extends Operator {

    public Audit(ScheduleBrigeAdapter schedule) {
        super(schedule);
    }

    @Override
    public void done() {
        if(schedule.result.value() && schedule instanceof Link){
            String eventId = schedule.eventId;
            System.out.println("通过事件 id [" + eventId + "] 获取日程 id " + UUID.randomUUID().toString());
            System.out.println("根据事件 id [" + eventId + "] 获取发布设备 ");
            System.out.println("发布");
            return ;
        }

        System.out.println("修改日程状态");
    }
}
