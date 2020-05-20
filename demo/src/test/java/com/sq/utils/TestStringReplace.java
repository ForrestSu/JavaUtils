package com.sq.utils;

import java.util.HashMap;
import java.util.Map;

public class TestStringReplace {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("animal", "quick brown fox");
        map.put("target", "lazy dog");
        String template = "The ${animal} jumped over the ${target}.";
        String resolvedString = ReplaceUtil.resolve(template, map);
        System.out.println(resolvedString);
    }
}
