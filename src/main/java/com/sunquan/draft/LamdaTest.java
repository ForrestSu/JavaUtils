package com.sunquan.draft;

import java.util.function.Consumer;
import java.util.function.Function;

public class LamdaTest {


    static class UserT{
        public UserT(String name)
        {
           this.sname = name;
        }
        public boolean setName(String name)
        {
            sname = name;
            return true;
        }
        @Override
        public String toString() {
            return sname;
        }
        private String sname;
    }
    // functional interface
    @FunctionalInterface
    interface StringMapper {
        int map(String str);
    }

    public static void main(String[] args) {

        testConsumer();


        StringMapper mapper = (String str) -> str.length();
        System.out.println(mapper.map("chen"));

        // Function <T, R>
        Function<Integer, Integer> square1 = (x) -> x * x;
        System.out.println(square1.apply(5));

        // chaining three functions
        Function<Long, Long> chainedFunction = ((Function<Long, Long>) (x -> x * x))
                .andThen(x -> x + 1)
                .andThen(x -> x * x);
        // .andThen();
        System.out.println(chainedFunction.apply(3L));

        java.util.function.Predicate<Integer> greaterThanTen = (x)-> { return x > 10;};

        System.out.println(greaterThanTen.test(15));
    }

    public static void testConsumer(){
    UserT userT = new UserT("zm");
    //接受一个参数
    Consumer<UserT> userTConsumer = userT1 -> {userT1.setName("zmChange");};
    userTConsumer.accept(userT);
     //////
    System.out.println(userT);
}

}