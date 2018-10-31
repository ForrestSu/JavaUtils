package com.sunquan.tools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sunquan.mail.SendEmail;


public class TaobaoHaveShop {
	private static String skuId_While = "3201601885401";
	private static String skuId_Gray = "3201601885402";
	private static String skuId_Blue = "3201601885403";
	
	private static String skuId_While_1A = "3946976899575";
	private static String skuId_Gray_1A = "3946976899576";
	
	public static int FindStock(String html, String skuid) {
		int ipos = html.lastIndexOf(skuid);
		String stockFregment = html.substring(ipos - 30, ipos + skuId_Gray_1A.length());
		//System.out.println(stockFregment);
		String items[] = stockFregment.split(",");
		int stock = -1;
		for (String item : items) {
			if (item.contains("stock")) {
				stock = Integer.valueOf(item.split(":")[1]);
			}
		}
		return stock;
	}
	
	public static boolean QueryStock() {
		URL url = null;
		try {
			url = new URL( "https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.6.4685711brJdYZs&id=527075698825&areaId=440305&user_id=1714128138&cat_id=2&is_b=1&rn=007ad23c7c7dc63021f16154a045360d");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Document doc;
		try {
			doc = Jsoup.parse(url, 1500);
			String html = doc.data();
			int gray_1A = FindStock(html, skuId_Gray_1A);
			int gray = FindStock(html, skuId_Gray);
			int blue = FindStock(html, skuId_Blue); //162
			System.out.println(Calendar.getInstance().getTime()+ " gray => "+ gray + ", gray_1A => "  + gray_1A +", blue => "+ blue);
			if (gray_1A + gray > 0) {
				System.err.println("Complete: occur ==> " + (gray_1A + gray));
				
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void main(String[] args) throws IOException {
	
		 final long timeSpanSec = 3;
	        final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
	        service.scheduleAtFixedRate(() -> {
	          if( QueryStock())
	          {
	        	  service.shutdown();
	        	  SendEmail.TestEmail();
	          }
	        }, timeSpanSec, 3, TimeUnit.SECONDS);
	}
}
