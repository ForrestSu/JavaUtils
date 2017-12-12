package com.sunquan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils 
{
   public static String CurDate()
   {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return  sdf.format(new Date());
   }
   public static String CurTime()
   {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return  sdf.format(new Date());
   }
   public static int iCurTime()
   {
	    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return  Integer.valueOf(sdf.format(new Date()));
   }
}
