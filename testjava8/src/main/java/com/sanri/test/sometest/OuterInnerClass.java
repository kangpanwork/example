package com.sanri.test.sometest;

/**
 * 静态内部类,非静态内部类测试
 */
public class OuterInnerClass {

    private String name;

    private static String imei;

    /**
     * 外部类不能拿到非静态内部类的成员
     * @return
     */
    public int getInnerAge(){
//        return age; // 报错,拿不到这个属性
        String idcard = StaticInner.idcard;     // 可以访问静态属性
        return new Inner().age;
    }

    public static void hello(){
//        Inner inner = new Inner();        // 报错,拿不到实例类的
        StaticInner staticInner = new StaticInner();
    }

    public void normalMethod(){
        class Inner {
            public String get(){
                class InnerInner{
                    public void method(){}
                };

                return "abc";
            }
        }
    }

    public class Inner{
        private int age;
//        private static String idcard;     // 报错,非静态内部类不能有静态属性,静态代码块,静态方法
//        static {
//
//        }
//        public static int getAge(){};

        public String getOuterName(){
            return name;
        }

        public String invokeOuterMethod(){
            hello();
            System.out.println(name);
            return getInnerAge()+"";
        }
    }

    public static class StaticInner{
        private static String idcard;

        public String mergeIdcard(){
//            System.out.println(name);     // 报错,取不到外部类的 name ,因为这个类的对象可以先构建
            System.out.println(imei);       // 可以访问外部类的静态成员
            hello();
            return idcard;
        }
    }
}
