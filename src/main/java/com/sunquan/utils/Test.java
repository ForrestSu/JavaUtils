package com.sunquan.utils;

import java.util.Date;

public class Test {

    public static void main(String[] args) {

        Date dt = new Date(1553568933473L);
        System.out.println(dt.toLocaleString());

        // double time_sec = (1553568933473L + 8*60*60*1000)% (24 * 60 * 60 * 1000)) / 1000.0;
    }
}
