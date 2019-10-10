package com.sunquan.files;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadUtil {

    /**
     * -从网络下载图片
     * 
     * @param url  "http://example.com/image.jpg"
     * @param dest "D:/File/To/Save/To/image.jpg"
     * @return
     */
    public static boolean DownloadImage(String imageUrl, String dest) {
        boolean is_ok = false;
        try {
            try (InputStream in = new URL(imageUrl).openStream()) {
                Files.copy(in, Paths.get(dest));
            }
            is_ok = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is_ok;
    }
}
