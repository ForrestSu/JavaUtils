package com.sq.redis;

import java.nio.charset.Charset;
import java.util.HashMap;

import redis.clients.jedis.Jedis;

public class JedisTest {

	public static void TestAsync() {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		byte[] value = new String("孙权hello").getBytes(Charset.forName("UTF-8"));
		jedis.set("bytekey".getBytes(), value);
		
		byte[] key= "bytearray".getBytes();
		byte[] field = "sunquan".getBytes();
		HashMap<byte[], byte[]> hash = new HashMap<byte[],byte[]>();
		hash.put(field, value);
		Long ret = jedis.hset(key, hash);
		
		System.out.println("ret == "+ ret);
	}

	public static void main(String[] args) {
		TestAsync();
		System.out.println("OK!");
	}
}
