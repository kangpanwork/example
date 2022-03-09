package com.sanri.test.deginmodel.state;

public class PayOrderState extends OrderState {
    @Override
    public void doSomething(RunOrder runOrder) {
        System.out.println("支付订单");
        runOrder.setOrderState(new FinishOrderState());
    }
}
