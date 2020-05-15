package com.sq.redis;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.ByteArrayCodec;

public class AsyncRedis {

	public static void TestAsyncBytes() {
		RedisClient client = RedisClient.create("redis://localhost:6379/0");
		RedisAsyncCommands<byte[], byte[]> commands = client.connect(ByteArrayCodec.INSTANCE).async();
		RedisFuture<Map<byte[], byte[]>> future = commands.hgetall("bytearray".getBytes());
		System.out.println("commands.isOpen() == " + commands.isOpen());
		
		future.thenAcceptAsync(result -> {
			for (Entry<byte[], byte[]> entry : result.entrySet()) {
				try {
					String tmpkey = new String(entry.getKey(),"UTF-8");
					String tmpval = new String(entry.getValue(),"UTF-8");
					System.out.println("Key = " + tmpkey + ", Value = " + tmpval);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
		
		while(true) {
			System.out.println("test...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(future.isDone()) {
				break;
			}
		}
	}
	
	public static void TestAsync() {
		RedisClient client = RedisClient.create("redis://localhost:6379/0");
		RedisAsyncCommands<String, String> commands = client.connect().async();
		RedisFuture<Map<String, String>> future = commands.hgetall("bytearray");
		System.out.println("commands.isOpen() == " + commands.isOpen());
		
		future.thenAcceptAsync(result -> {
			System.out.println(result);
		});
		
		while(true) {
			System.out.println("test...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/*
		try {
			Map<String, String> ret = future.get();
			System.out.println("retMap == " + ret);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}*/
	}

	public static void main(String[] args) {
		//TestAsync();
		TestAsyncBytes();
		System.out.println("OK!");
	}
}
