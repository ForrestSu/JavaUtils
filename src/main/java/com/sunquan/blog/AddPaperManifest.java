package com.sunquan.blog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AddPaperManifest {

	/*
	 * 
---
title: 被拒绝，也是一种肯定
date: 2017-03-12 23:02:12
tags: reject
categories: 前端
---
	 */
	public boolean AddHeader(File file, String category)
	{
		String fname= file.getName();
		if(fname.endsWith(".md"))
		{
			String no_prefix_name = fname.substring(0, fname.length()-3);
			BufferedReader br=null;
			StringBuilder sb = new StringBuilder();
			try {
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);
				String buf = br.readLine();  
				if(buf.startsWith("---"))
				{
					System.out.println(fname + " has added,ignore!");
				}else{
					
					sb.append("---")
					  .append("\ntitle: "+ no_prefix_name)
					  //.append("\ndate: ")
					  .append("\ntags: ")
					  .append("\ncategories: "+category)
					  .append("\n---\n");
					while (buf != null) { 
						sb.append("\n").append(buf);
		                buf = br.readLine();  
		            } 
					sb.append("\n");
					br.close();
					fr.close();
		            // 输出到文件
					
					// 设置成尾部追加方式  
		            FileWriter fw = new FileWriter(file, false);  
		            BufferedWriter writer = new BufferedWriter(fw); 
		            writer.write(sb.toString());
		            writer.flush(); 
		            writer.close();
		            fw.close();
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		return false;
	}
	 //递归遍历当前文件夹下的所有文件  
    public int showAllSubFile(String path, String category) {  
        int success_cnt=0;
    	File f = new File(path);  
        String[] list = f.list();  
        //int idx=0;
        for (String fname : list) {  
            //System.out.println((++idx)+":"+s);  
            File subf = new File(f.getPath() + File.separator + fname);  
            // 如果当前s所代表的是文件夹  
            if (subf.isDirectory())  {
                showAllSubFile(subf.getPath(),fname);  
            }else {  
            	if(category!=null)
            	{
            		if(AddHeader(subf, category))
            			++success_cnt;
            	}
            }  
        }
        return success_cnt;
    }  
    
	public static void main(String[] args) {
		String path= "E:\\openSourceCode\\blog\\source\\_posts";
		AddPaperManifest obj = new AddPaperManifest();
		int ret = obj.showAllSubFile(path,null);
		System.out.println(ret +" successful!");
	}
}
