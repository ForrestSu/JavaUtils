package com.sq.generic.interf;


public class Derived implements BaseInterface<String> {

    @Override
    public void check(String d) {
        System.out.println("String!");
    }
}
