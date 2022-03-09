package com.sanri.test.deginmodel.test;

import com.sanri.test.deginmodel.state.RunOrder;
import com.sanri.test.deginmodel.state.StartOrderState;
import org.junit.Test;

public class StateTest {
    @Test
    public void testState(){
        RunOrder runOrder = new RunOrder(new StartOrderState());
        runOrder.value = 2;
        runOrder.printState();
        runOrder.printState();
        runOrder.printState();
        runOrder.printState();
    }
}
