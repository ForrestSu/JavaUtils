package com.sq.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class TestJackJson {

    /**
     * 将java POJO 对象序列化为json字符串
     */
    public static String serialize_POJO() {
        String[] strArr = new String[]{"test1", "test2"};
        ObjectMapper mapper = new ObjectMapper();

        String json = "";
        try {
            json = mapper.writeValueAsString(strArr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("serial: " + json);
        return json;
    }

    /**
     * 将字符串解析为Java POJO 对象
     *
     * @return
     */
    public static void deserialize(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<String> strArr = mapper.readValue(json, new TypeReference<List<String>>() {
            });
            System.out.println(strArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final String json = serialize_POJO();
        deserialize(json);
    }
}
