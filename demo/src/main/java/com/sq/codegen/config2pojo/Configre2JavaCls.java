package com.sq.codegen.config2pojo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Configre2JavaCls {

    public static boolean Convert(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (true) {
                String buf = br.readLine();
                if (buf == null) {
                    break;
                }
                if (buf.length() < 3 || buf.startsWith("#")) {
                    continue;
                }
                if (buf.contains("=")) {
                    String[] params = buf.split("=");
                    if (params.length == 2) {
                        String skey = params[0].replaceAll("[.]", "_");
                        skey = skey.replaceFirst("sftp_", "").toUpperCase();
                        String convert = "public static final String " + skey + "_PROP_NAME"
                                + " = \"" + params[0] + "\";";
                        
                        String convert_default = "public static final String " + skey + "_DEFAULT"
                                + " = \"" + params[1] + "\";\n";
                        System.out.println(convert);
                        System.out.println(convert_default);
                    } else {
                        System.err.println("ivalid params buf:" + buf);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
        String file = "config/sftp_task.properties";
        Convert(file);
    }
}
