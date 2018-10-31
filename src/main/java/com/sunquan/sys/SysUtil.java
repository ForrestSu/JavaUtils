package com.sunquan.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class SysUtil {
    /**
     * 执行一个shell 脚本
     * @param shcmd
     *            "sh /home/trade/build.sh"
     */
    public static void DoCmd(String shcmd) {
        try {
            Process ps = Runtime.getRuntime().exec(shcmd);
            ps.waitFor();
            // 获取shell脚本的输出
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            BufferedReader brerr = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
            StringBuffer sbuff = new StringBuffer();
            //std output
            String line;
            while ((line = br.readLine()) != null) {
                sbuff.append(line).append(System.lineSeparator());
            }
            System.out.print(sbuff.toString());
            
            //error output
            sbuff.delete(0, sbuff.length());
            while ((line = brerr.readLine()) != null) {
                sbuff.append(line).append(System.lineSeparator());
            }
            System.err.print(sbuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load system properties from a given set of filenames or URLs.
     *
     * @param filenamesOrUrls that holds properties
     * @see #loadPropertiesFile(String)
     */
    public static void loadPropertiesFiles(final String[] filenamesOrUrls) {
        for (final String filenameOrUrl : filenamesOrUrls) {
            loadPropertiesFile(filenameOrUrl);
        }
    }

    /**
     * Load system properties from a given filename
     * <p>
     * File is searched for in resources.
     *
     * @param filename that holds properties
     */
    public static void loadPropertiesFile(final String filename) {
        final Properties properties = new Properties(System.getProperties());
        final File file = new File(filename);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                properties.load(in);
            } catch (final Exception ignore) {
            }
        }
        System.setProperties(properties);
    }
}
