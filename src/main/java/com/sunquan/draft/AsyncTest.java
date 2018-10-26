package com.sunquan.draft;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CompletableFuture;

public class AsyncTest {
	
	// 1、 创建一个完成的CompletableFuture
	static void completedFutureExample() {
	    CompletableFuture<String> cf = CompletableFuture.completedFuture("message");
	    assertTrue(cf.isDone());
	    assertEquals("message", cf.getNow(null));
	    System.out.println("OK!");
	}
	
	static void runAsyncExample() {
	    CompletableFuture cf = CompletableFuture.runAsync(() -> {
	        assertTrue(Thread.currentThread().isDaemon());
	        //randomSleep();
	        System.out.println("inner tid: "+ Thread.currentThread().getId());
	        try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    });
	    assertFalse(cf.isDone());
	    System.out.println("tid: "+ Thread.currentThread().getId());
	    System.out.println("first assert! sleeping... ");
	    try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    assertTrue(cf.isDone());
	    System.out.println("complete!");
	}
	
	public static void main(String[] args) {
		//completedFutureExample();
		runAsyncExample();
	}
}
