package com.sanri.test.reflect;

/**
 * 用于方法反射,测试继承方法的获取
 */
public abstract class MethodReflectBase {

    public void name(String name){};

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
