package com.sanri.test.java8;

import org.junit.Test;

import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Lambda20190924 {

    @Test
    public void main(){
        Consumer out = x -> System.out.println(x);
        BinaryOperator<Integer> binaryOperator  = (x,y) -> {return x > y ? 1 : -1 ;};

        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String o) {
                System.out.println(o);
            }
        };


        Supplier<DateTimeConvert> supplier = () -> new DateTimeConvert();
        DateTimeConvert dateTimeConvert = supplier.get();
        Consumer<DateTimeConvert> consumer1 = (x) -> x.toCalendar();
        consumer1.accept(dateTimeConvert);

        Supplier<DateTimeConvert> supplier2 = DateTimeConvert::new;
        Consumer<DateTimeConvert> consumer2 = DateTimeConvert::toCalendar;
    }
}
