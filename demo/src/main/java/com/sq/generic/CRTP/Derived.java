package com.sq.generic.CRTP;

/**
 * [CRTP](https://en.wikipedia.org/wiki/Curiously_recurring_template_pattern)
 */
public class Derived extends Base<Derived> {

    public void check(Derived d) {
        if(this == d){
            System.out.println("is same object");
        }
        System.out.println("CRTP call");
    }
}
