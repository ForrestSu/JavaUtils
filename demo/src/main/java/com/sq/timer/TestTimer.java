package com.sq.timer;

import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {

    public static void main(String[] args) {
        Timer t1 = new Timer();
        t1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // TASK 1
                System.out.println("Task1 tid " + Thread.currentThread().getId());
            }
        }, 0, 2000);

        Timer t2 = new Timer();
        t2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task2 tid " + Thread.currentThread().getId());
            }
        }, 0, 3000);
    }
}
