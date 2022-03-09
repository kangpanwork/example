package com.sanri.test.reflect;

import com.sanri.test.jvm.TestRef;
import com.sanri.test.reflect.beans.*;
import com.sanri.test.reflect.beans.full.Primitive;
import com.sanri.test.reflect.beans.full.PrimitiveWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

public class TestReflect {
	public List<Staff> staff;

	public List<Device> devices(List<String> deviceIds) throws IOException {

		return null;
	}

	/**
	 * 如何获取 Type 接口 ,Type 是类型的顶层接口 Class 只是它的一种最常见的实现
	 * 其它实现都差不多是针对泛型的,如  ParameterizedType , GenericArrayType , TypeVariable, WildcardType
	 */
	@Test
	public void testGetType() throws NoSuchMethodException, NoSuchFieldException {
		Type genericSuperclass = TestReflect.class.getGenericSuperclass();		// 获取 TestReflect 继承的类的 Type
		Type[] genericInterfaces = TestReflect.class.getGenericInterfaces();	// 获取 TestReflect 实现的类的 Type 列表

		Method test = TestReflect.class.getDeclaredMethod("devices",List.class);
		Type[] genericParameterTypes = test.getGenericParameterTypes();			// 获取方法参数的所有 Type 列表
		Type genericReturnType = test.getGenericReturnType();					// 获取返回值 Type
		Type[] genericExceptionTypes = test.getGenericExceptionTypes();			// 获取异常 Type 列表

		Field staff = TestReflect.class.getDeclaredField("staff");		// 获取类上属性的 Type
		Type genericType = staff.getGenericType();

		System.out.println();
	}

    public static void test(TestReflect p0,
    		List<TestReflect> p1,
    		Map<String,TestReflect> p2,
    		List<String>[] p3,
    		Map<String,TestReflect>[] p4,
    		List<? extends TestReflect> p5,
    		Map<? extends TestReflect,? super TestReflect> p6
    		//T p7
    		){
    	
    }
 
	public static void main(String[] args) {
    	
		Method[] methods=TestReflect.class.getMethods();
 
		for(int i=0;i<methods.length;i++){
			Method oneMethod=methods[i];
			
			if(oneMethod.getName().equals("test")){
				Type[] types=oneMethod.getGenericParameterTypes();
				
				//第一个参数，TestReflect p0
				Class type0=(Class)types[0];
				System.out.println("type0:"+type0.getName());
				
				//第二个参数，List<TestReflect> p1
				Type type1=types[1];
				System.out.println("是否是 Class 类型:"+(type1 instanceof Class));
				System.out.println(((ParameterizedType) type1).getRawType());
				System.out.println(((ParameterizedType) type1).getOwnerType());

				Type[] parameterizedType1=((ParameterizedType)type1).getActualTypeArguments();
				Class parameterizedType1_0=(Class)parameterizedType1[0];
				System.out.println("parameterizedType1_0:"+parameterizedType1_0.getName());
				
				//第三个参数，Map<String,TestReflect> p2
				Type type2=types[2];
				Type[] parameterizedType2=((ParameterizedType)type2).getActualTypeArguments();
				Class parameterizedType2_0=(Class)parameterizedType2[0];
				System.out.println("parameterizedType2_0:"+parameterizedType2_0.getName());
				Class parameterizedType2_1=(Class)parameterizedType2[1];
				System.out.println("parameterizedType2_1:"+parameterizedType2_1.getName());
				
 
				//第四个参数，List<String>[] p3
				Type type3=types[3];
				Type genericArrayType3=((GenericArrayType)type3).getGenericComponentType();
				ParameterizedType parameterizedType3=(ParameterizedType)genericArrayType3;
				Type[] parameterizedType3Arr=parameterizedType3.getActualTypeArguments();
				Class class3=(Class)parameterizedType3Arr[0];
				System.out.println("class3:"+class3.getName());
				
				//第五个参数，Map<String,TestReflect>[] p4
				Type type4=types[4];
				Type genericArrayType4=((GenericArrayType)type4).getGenericComponentType();
				ParameterizedType parameterizedType4=(ParameterizedType)genericArrayType4;
				Type[] parameterizedType4Arr=parameterizedType4.getActualTypeArguments();
				Class class4_0=(Class)parameterizedType4Arr[0];
				System.out.println("class4_0:"+class4_0.getName());
				Class class4_1=(Class)parameterizedType4Arr[1];
				System.out.println("class4_1:"+class4_1.getName());
				
 
				//第六个参数，List<? extends TestReflect> p5
				Type type5=types[5];
				Type[] parameterizedType5=((ParameterizedType)type5).getActualTypeArguments();
				Type[] parameterizedType5_0_upper=((WildcardType)parameterizedType5[0]).getUpperBounds();
				Type[] parameterizedType5_0_lower=((WildcardType)parameterizedType5[0]).getLowerBounds();
				
				//第七个参数，Map<? extends TestReflect,? super TestReflect> p6
				Type type6=types[6];
				Type[] parameterizedType6=((ParameterizedType)type6).getActualTypeArguments();
				Type[] parameterizedType6_0_upper=((WildcardType)parameterizedType6[0]).getUpperBounds();
				Type[] parameterizedType6_0_lower=((WildcardType)parameterizedType6[0]).getLowerBounds();
				Type[] parameterizedType6_1_upper=((WildcardType)parameterizedType6[1]).getUpperBounds();
				Type[] parameterizedType6_1_lower=((WildcardType)parameterizedType6[1]).getLowerBounds();
								
			}
			
			
		}
		
	}

	@Test
	public void testEq(){
//    	List a= new ArrayList();
//		System.out.println(a.getClass() == Collection.class);
	}

	@Test
	public void testRandomData(){
    	RandomDataService randomDataService = new RandomDataService();

//		System.out.println(randomDataService.populateData(String.class));
//		System.out.println(randomDataService.populateData(int.class));
//		System.out.println(randomDataService.populateData(long.class));
//		System.out.println(randomDataService.populateData(Date.class));
//		System.out.println(randomDataService.populateData(BigDecimal.class));
//		Object strarr = randomDataService.populateData(String[].class);
//		Object intarr = randomDataService.populateData(int[].class);
//		Object longarr = randomDataService.populateData(long[].class);
//		Object intEarr =  randomDataService.populateData(Integer[].class);
//		System.out.println(StringUtils.join(strarr,','));
//		System.out.println(StringUtils.join(intarr,','));
//		System.out.println(StringUtils.join(longarr,','));
//		System.out.println(StringUtils.join(intEarr,','));

//		System.out.println(randomDataService.populateData(Primitive.class));
//		System.out.println(randomDataService.populateData(PrimitiveWrapper.class));
//		System.out.println(randomDataService.populateData(Staff.class));
//
//		System.out.println(randomDataService.populateData(StringArray.class));

//        System.out.println(randomDataService.populateData(Menu.class));

		System.out.println(randomDataService.populateData(CycleRefA.class));
	}

	@Test
	public void testStaticMethodReflect() throws NoSuchMethodException {
		Method staticMethod = MethodReflect.class.getDeclaredMethod("staticMethod", String.class);
		System.out.println(staticMethod);
	}

	/**
	 * 内部类反射的测试
	 */
	@Test
	public void testInnerClassReflect(){
		// 该方法返回当前Class声明的public，private ，default，private的内部类
		Class<?>[] declaredClasses = MethodReflect.class.getDeclaredClasses();
		// 该方法获取的是包含父类和当前类声明的public类型的内部类，注意是public的
		Class<?>[] classes = MethodReflect.class.getClasses();

		// 内部类使用,获取外部类是谁
		Class<?> declaringClass = MethodReflect.Inner.class.getDeclaringClass();


		System.out.println(declaredClasses);
		System.out.println(classes);
		System.out.println(declaringClass);
	}

}