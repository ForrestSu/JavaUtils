package com.sq.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class ReplaceUtil {

    /**
     * 使用 StringSubstitutor 进行字符串替换
     */
    public static String resolve(String template, Map<String, String> params) {
        StringSubstitutor sub = new StringSubstitutor(params);
        String result = "";
        try {
            result = sub.replace(template);
        } catch (Exception e) {
            System.err.println("参数 " + params.toString() + " 与定义的模板【" + template + "】不匹配");
            e.printStackTrace();
        }
        return result;
    }

}
