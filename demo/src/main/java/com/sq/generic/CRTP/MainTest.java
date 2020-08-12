package com.sq.generic.CRTP;


/**
 * Java 泛型测试
 */
public class MainTest {

    public static void main(String[] args) {
        Derived derived = new Derived();
        derived.check(derived);
    }
}
