package com.sanri.test.deginmodel.brige;

import java.util.List;

public class Recall extends Operator{
    public Recall(ScheduleBrigeAdapter schedule) {
        super(schedule);
    }

    @Override
    public void done() {
        if(schedule instanceof Link){
            // 修改日程状态
            boolean value = schedule.result.value();
            if (value){
                System.out.println("修改日程状态为撤回成功");
            }
            return ;
        }

        if (schedule.result.value()){
            System.out.println("删除发布记录");
        }

    }
}
