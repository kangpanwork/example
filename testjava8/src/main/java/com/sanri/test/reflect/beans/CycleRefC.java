package com.sanri.test.reflect.beans;

import lombok.Data;

@Data
public class CycleRefC {
    private CycleRefA cycleRefA;
}
