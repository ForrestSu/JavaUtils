package com.sq.generic.CRTP;


public abstract class Base<T extends Base<T>> {
    public abstract void check(T d);
}