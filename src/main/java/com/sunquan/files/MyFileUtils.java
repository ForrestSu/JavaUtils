package com.sunquan.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class MyFileUtils {

	// 读取一个目录下的所有文件
	public static Set<String> ListFiles(String spath) {
		Set<String> result = new TreeSet<String>();
		File dir = new File(spath);
		if (dir.isDirectory() && dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					result.add(files[i].getName());
				}
			}
		}
		return result;
	}

	/**
	 *  写之前确保文件所在的文件夹已经创建,否则会写入异常
	 * @param fileName
	 * @param data
	 * @param len
	 * @return true when success, false when write fail!
	 */
	public static boolean WriteBytes2File(String fileName, byte[] data) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			out.write(data, 0, data.length);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: can't find file! " + e.getMessage());
		} catch (IOException e) {
			System.err.println("ERROR: write2file!" + e.getMessage());
		}
		return false;
	}
 
	/**
	 * 从文件中读取字节码
	 * @param fileName 
	 * @return byte[] Read Successful
	 *     null Failure
	 */
	public static byte[] ReadFileAsBytes(String fileName) {
		File file = new File(fileName);
		int len = (int) file.length();
		if (len <= 0)
			return null;
		byte[] b = new byte[len];
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			int count = in.read(b, 0, len);
			if (count != len) {
				System.err.println("ERROR: read file, need " + count + " bytes, but just read " + len + " bytes actually!");
			}
			in.close();
			return b;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
