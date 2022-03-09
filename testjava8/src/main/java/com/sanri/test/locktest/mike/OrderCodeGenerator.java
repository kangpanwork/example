package com.sanri.test.locktest.mike;

public class OrderCodeGenerator {
    static int orderNo = 1;
    public int getOrderCode(){
        return orderNo++;
    }
}
