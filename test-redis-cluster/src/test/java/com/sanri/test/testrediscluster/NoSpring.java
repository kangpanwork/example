package com.sanri.test.testrediscluster;

import org.junit.Test;

public class NoSpring {
    @Test
    public void test(){
        try{
            System.exit(-1);
        }finally {
            System.out.println("结束 ");
        }
    }
}
