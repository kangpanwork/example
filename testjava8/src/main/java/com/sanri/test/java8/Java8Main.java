package com.sanri.test.java8;

import org.junit.Test;
import org.junit.internal.runners.statements.RunAfters;

import java.util.*;
import java.util.function.*;

public class Java8Main {
    public static void main(String[] args) {
        Car car = Car.create(Car::new);
        Car car2 = Car.create(() -> new Car());

        List<Car> cars = Arrays.asList(car,car2);
        cars.forEach(Car::collide);

        cars.forEach(car3 -> Car.collide(car3));

        Car police = Car.create(Car::new);
        cars.forEach(police::follow);

        cars.forEach(Car::repair);


//        Optional<Object> o = Optional.of(null);       NullPoint

        Optional< String > fullName = Optional.ofNullable( null );
        System.out.println( "Full Name is set? " + fullName.isPresent() );
        System.out.println( "Full Name: " + fullName.orElseGet( () -> "[none]" ) );
        System.out.println( fullName.map( s -> "Hey " + s + "!" ).orElse( "Hey Stranger!" ) );

        System.out.println("-------------------");
        // Function<T, R> -T作为输入，返回的R作为输出
        Function<String,String> function = (x) -> {System.out.print(x+": ");return "Function";};
        System.out.println(function.apply("hello world"));

        UnaryOperator<String> unaryOperator = x -> x + 2;
        System.out.println(unaryOperator.apply("9420-"));

        //Predicate<T> -T作为输入，返回的boolean值作为输出
        Predicate<String> pre = (x) ->{System.out.print(x);return false;};
        System.out.println(": "+pre.test("hello World"));

        System.out.println("consumer---- ");
        //Consumer<T> - T作为输入，执行某种动作但没有返回值
        Consumer<String> con = (x) -> {System.out.println(x);};
        Consumer<String> otherConsumer = x -> System.out.println(x+2);
        con.accept("hello world");
        con.andThen(otherConsumer).accept("hello world ");

        //Supplier<T> - 没有任何输入，返回T
        Supplier<String> supp = () -> {return "Supplier";};
        System.out.println(supp.get());

        //BinaryOperator<T> -两个T作为输入，返回一个T作为输出，对于“reduce”操作很有用
        BinaryOperator<String> bina = (x, y) ->{System.out.print(x+" "+y);return "BinaryOperator";};
        System.out.println("  "+bina.apply("hello ","world"));

        // true
        IntPredicate intPredicate = (x) -> {return x%2 == 0;};
        IntPredicate isTen = x -> x ==10;
        System.out.println(intPredicate.or(isTen).test(10));

        UnaryOperator<String> string2String = x -> x + x;
        String a = string2String.apply("a");
        System.out.println("isaa:"+a);
        Function<String,Integer> string2Int = x -> Integer.parseInt(x);
        System.out.println(string2Int.apply("2"));

        String obj = "";
        Optional<String> canUseObj = Optional.ofNullable(obj);
        canUseObj.ifPresent(System.out::println);

    }

    @Test
    public void testOptional() throws  RuntimeException{
//        boolean b = !Optional.ofNullable(null).isPresent();
//        System.out.println(b);


        Optional.ofNullable(null).orElseThrow(() -> new RuntimeException("空对象"));
    }

    @Test
    public void testReduce(){
        List<Integer> values = Arrays.asList(1,2,3,4,5);
        Integer reduce = values.stream().reduce(0, (i, j) -> i + j);
        System.out.println(reduce);
    }

    /**
     * List 数据转 map
     */
    @Test
    public void testListConvertMap(){
        List<String> deviceIds = new ArrayList<String>(){{
            add("a");add("b");add("c");
        }
        };
        Map<String,String> mirror = new HashMap<String,String>(){{
            put("a","1");
            put("b","2");
        }};

        // toMap 不允许空值
//		Map<String, String> terminalNoMap = deviceIds.stream().collect(Collectors.toMap(deviceNo -> deviceNo, deviceNo -> mirror.get(deviceNo)));
//		System.out.println(terminalNoMap);
        Map<String, String> terminalNoMap = deviceIds.stream().collect(HashMap::new, (m, v) -> m.put(v, mirror.get(v)), HashMap::putAll);
        System.out.println(terminalNoMap);
    }
}
