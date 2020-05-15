package com.sq.draft;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceAsync {

	public static class ServiceA{
		public int sayHello(String name) {
			System.out.println(Thread.currentThread().getName()+": hello "+name);
			return 0;
		}
		public void throwException() {
			throw new NullPointerException();
		}
	}

	public static void main(String[] args) {
		ServiceA a=new ServiceA();
		String name="demo";
		//同步调用
		int retVal = a.sayHello("demo");
		
		ExecutorService threadPool=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		//异步调用,
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->a.sayHello(name));
		future.whenComplete((result,e)->{
			if(e!=null) {
				e.printStackTrace();
			}
		});
		//异步调用,指定线程池执行方法,注意supplyAsync,runAsync的不同,前者有返回值,后者没有
		CompletableFuture<Void> voidFuture = CompletableFuture.runAsync(()->a.throwException(),threadPool);
		voidFuture.whenComplete((result,e)->{
			if(e!=null) {
				e.printStackTrace();
			}
		});
	}
}
