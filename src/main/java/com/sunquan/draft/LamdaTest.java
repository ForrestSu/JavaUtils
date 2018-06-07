package com.sunquan.draft;

import java.util.function.Function;

public class LamdaTest {

        // functional interface
    @FunctionalInterface
    interface StringMapper{
        int map(String str);
    }

    public static void main(String[] args){
        StringMapper mapper = (String str) -> str.length();
        System.out.println(mapper.map("chen"));

        // Function <T, R>
        Function<Integer,Integer> square1=(x)->x*x;
        System.out.println(square1.apply(5));


        // chaining three functions
        Function<Long, Long> chainedFunction = ((Function<Long, Long>)(x -> x * x))
                .andThen(x -> x + 1)
                .andThen(x -> x * x);
                //.andThen();
        System.out.println(chainedFunction.apply(3L));

        java.util.function.
        // predictates
        Predicate<Integer> greaterThanTen=x->x>10;
        System.out.println(greaterThanTen.test(15));
    }

}