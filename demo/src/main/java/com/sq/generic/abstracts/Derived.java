package com.sq.generic.abstracts;


public class Derived extends Base<String> {

    @Override
    public void check(String d) {
        System.out.println("String!");
    }
}
