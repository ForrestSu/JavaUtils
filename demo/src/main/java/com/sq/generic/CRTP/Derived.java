package com.sq.generic.CRTP;

/**
 * c
 */
public class Derived extends Base<Derived> {

    public void check(Derived d) {
        if(this == d){
            System.out.println("is same object");
        }
        System.out.println("CRTP call");
    }
}
