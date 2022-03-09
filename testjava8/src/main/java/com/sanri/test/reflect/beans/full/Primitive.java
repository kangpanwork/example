package com.sanri.test.reflect.beans.full;

import lombok.Data;
import org.springframework.util.ClassUtils;

/**
 * 原始类型
 */
@Data
public class Primitive {
    private byte a;
    private short b;
    private int c;
    private long d;

    private float e;
    private double f;

    private char g;
    private boolean h;
}
