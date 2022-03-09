package com.sanri.test.reflect.beans;

import lombok.Data;

@Data
public class CycleRefA {
    private CycleRefB cycleRefB;
}
