package com.sunquan.files;

import java.io.*;


/**
 * -需求： 转换Blog 
 * 1 先访问 https://www.jianshu.com/u/6a0dba2307a4 获取每篇文章的名称+更新时间
 * 2 读取本地的文件，为每篇blog 追加 如下 Front Matter
---
title: "文章名称"    //提取文件名即可(去除后缀)
date: 2016-01-26T00:03:04
updated:          // 暂且先等于date
toc: true
comments: true
categories: [Git] // 采用文件夹名称
tags: [github]    // 从文章名称中提取关键字
---
 * 
 * 3 其他问题，怎么给文章摘要添加图片显示
  一种解决办法：在Front Matter 之后添加
 <img src="https://faithlove.github.io/pic/2018/RMTP_1/topPicPre.png" width=50% />
哇，漂亮的小姐姐(❤ ω ❤)
<!--more-->
 */


public class BlogConvertUtil {

	static FileWriter fw;
	static BufferedWriter writer;
	static String headTitle = "＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝";
	static String endline="\n";
	public BlogConvertUtil() {}
	public BlogConvertUtil(String outputPath)
	{
		String os=System.getProperties().getProperty("os.name");
		if(os.startsWith("win")||os.startsWith("Win"))endline="\r\n";
		try {
			// 设置成尾部追加方式
			fw = new FileWriter(outputPath, true);
			writer = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param path  绝对路径
	 * @param filename 要读的文件名
	 */
	public void WriteToMyFile(String path, String filename) {
		
		if (filename.endsWith(".cpp") || filename.endsWith(".h")) {
			try {
				// writer.write(endline+headTitle+endline);
				writer.write("『" + filename + "』");
				// writer.write(endline+headTitle+endline);
				BufferedReader br = new BufferedReader(new FileReader(path));
				String buf = br.readLine();
				while (buf != null) {
					writer.write(buf + endline);
					buf = br.readLine();
				}
				// 输出到文件
				writer.flush();
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    //递归遍历当前文件夹下的所有文件
	public void showAllSubFile(String path) {
		File f = new File(path);
		String[] list = f.list();

		for (String s : list) {
			// System.out.println(s);
			File subf = new File(f.getPath() + File.separator + s);
			// 如果当前s所代表的是文件夹
			if (subf.isDirectory())
				showAllSubFile(subf.getPath());
			else {
				WriteToMyFile(subf.getPath(), s);
			}
		}
	}

	public static void main(String[] args) {
		
        System.out.println("start convert ....");
		/*输入文件夹路径*/
		String inpath="E:\\test";
		/*输出文件的路径*/
		String outpathString="E:\\test\\output.java";
		new BlogConvertUtil(outpathString).showAllSubFile(inpath);
		System.out.println("Export Complete.");
		// 最后关掉输出流
		try {
			if (writer != null)
				writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
