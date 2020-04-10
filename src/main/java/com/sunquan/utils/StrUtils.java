package com.sunquan.utils;

public class StrUtils {

    private static String toUpperFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    // "hello_world" => "helloWorld"
    public static String toCamelCase(String s) {
        String[] parts = s.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = (i > 0) ? toUpperFirstLetter(parts[i]) : parts[i];
            sb.append(part);
        }
        return sb.toString();
    }
}
