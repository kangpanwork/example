package com.sanri.test.deginmodel.test;

import com.sanri.test.deginmodel.brige.*;
import lombok.Data;
import org.junit.Test;

import java.util.UUID;
import java.util.logging.Handler;

/**
 * 审核成功 发布
 * 审核失败 改日程状态
 * 撤回成功 删除发布记录
 * 撤回失败 -
 *
 * 联动  改日程状态
 */
public class BirgeClient {
    @Test
    public void test(){
        vo(new HandlerDto("01", UUID.randomUUID().toString(), HandlerResult.AUDIT_PASS));
        System.out.println("-------------------------------");
        vo(new HandlerDto("02", UUID.randomUUID().toString(), HandlerResult.RECALL_SUCCESS));
        System.out.println("-------------------------------");
        vo(new HandlerDto("01", UUID.randomUUID().toString(), HandlerResult.REJECT));
        System.out.println("-------------------------------");
        vo(new HandlerDto("11", UUID.randomUUID().toString(), HandlerResult.AUDIT_PASS));
        System.out.println("-------------------------------");
        vo(new HandlerDto("11", UUID.randomUUID().toString(), HandlerResult.REJECT));
        System.out.println("-------------------------------");
        vo(new HandlerDto("12", UUID.randomUUID().toString(), HandlerResult.RECALL_SUCCESS));
    }

    @Test
    public void normal(){
        HandlerDto handlerDto = new HandlerDto("01", UUID.randomUUID().toString(), HandlerResult.AUDIT_PASS);
        String eventType = handlerDto.getEventType();
        char isLink = eventType.charAt(0);
        char op = eventType.charAt(1);
        if(isLink == '1'){
            if(op == '1'){
                switch (handlerDto.handlerResult){
                    case AUDIT_PASS:
                        break;
                    case REJECT:
                }
            }else if(op == '2'){

            }
        }else if (isLink == '0'){
            if(op == '1'){
                switch (handlerDto.handlerResult){
                    case AUDIT_PASS:
                        break;
                    case REJECT:
                }
            }else if(op == '2'){

            }
        }
    }

    void vo(HandlerDto handlerDto){
        ResultAdapter resultAdapter = new ResultAdapter(handlerDto);
        Operator operator = resultAdapter.convert();
        operator.done();
    }

    class ResultAdapter {
        HandlerDto handlerDto;

        public ResultAdapter(HandlerDto handlerDto) {
            this.handlerDto = handlerDto;
        }

        public Operator convert(){
            String eventId = handlerDto.getEventId();
            String eventType = handlerDto.getEventType();
            HandlerResult handlerResult = handlerDto.getHandlerResult();

            Result result = adapterResult(handlerResult);
            ScheduleBrigeAdapter schedule = adapterSchedule(eventType, result,eventId);
            Operator operator = adapterOperator(eventType, schedule);
            return operator;
        }

        public Result adapterResult(HandlerResult handlerResult){
            switch (handlerResult){
                case AUDIT_PASS:
                case RECALL_SUCCESS:
                    return new Pass();
            }
            return new Reject();
        }

        public Operator adapterOperator(String eventType, ScheduleBrigeAdapter schedule){
            char c = eventType.charAt(1);
            if(c == '1'){
                return new Audit(schedule);
            }
            return new Recall(schedule);
        }

        public ScheduleBrigeAdapter adapterSchedule(String eventType, Result result, String eventId){
            char c = eventType.charAt(0);
            if(c == '0'){
                return new Normal(result,eventId);
            }
            return new Link(result,eventId);
        }
    }

    @Data
    class HandlerDto{
        private String eventType;
        private String eventId;
        private HandlerResult handlerResult;

        public HandlerDto(String eventType, String eventId, HandlerResult handlerResult) {
            this.eventType = eventType;
            this.eventId = eventId;
            this.handlerResult = handlerResult;
        }
    }

    enum HandlerResult {
        AUTO_HANDLER(10),MANUAL_HANDLER(11), REMARK(12),
        AUDIT_PASS(21),REJECT(22),RECALL_SUCCESS(23),RECALL_FAIL(24),ERROR(-1);

        private Integer code;

        HandlerResult(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }

        /**
         * 枚举值解析
         * @param code
         * @return
         */
        public static HandlerResult parser(Integer code){
            if(code == null || code == -1){
                return ERROR;
            }
            for (HandlerResult value : HandlerResult.values()) {
                if(value.getCode().equals(code)){
                    return value;
                }
            }
            return ERROR;
        }
    }

}
