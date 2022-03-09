package com.sanri.test.reflect.beans;

import lombok.Data;

@Data
public class CycleRefB {
    private CycleRefC cycleRefC;
}
