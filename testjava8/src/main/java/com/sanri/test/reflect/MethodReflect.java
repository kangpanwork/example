package com.sanri.test.reflect;

/**
 * 用于方法的反射测试
 */
public class MethodReflect extends MethodReflectBase {
    /* 普通方法 */
    public void normalMethod(String name){}

    /* 静态方法 */
    public static void staticMethod(String name){}

    class Inner{
        public void normalMethod(String name){}
    }

    private class InnerPrivate{

    }
    protected class InnerProvide{

    }

    public class InnerPublic{

    }
}
