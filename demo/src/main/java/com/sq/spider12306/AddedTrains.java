package com.sq.spider12306;

import com.sq.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class AddedTrains {

    public void spider(final String strUrl) {
        try {
            URL url = new URL(strUrl);
            Document doc = Jsoup.parse(url, 8000);
            // System.out.println(doc);
            parseHtml(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseHtml(Document doc) {
        final String today = DateUtils.CurDateHuman();

        Element div = doc.getElementById("newList");
        Elements ElementsLi = div.select("ul > li");
        // System.out.println(ElementsLi.size());
        /**
         * <li><a href="../../zxdt_news/202009/t20200911_31864.html" target="_blank">中国铁路广州局集团有限公司关于2020年9月13日增开部分旅客列车的公告</a>
         * <img src="/mormhweb/images/new02.gif" border="0">　<span class="zxdt_time_in">(2020-09-11)</span></li>
         */
        /**
         * real link: https://www.12306.cn/mormhweb/zxdt_news/202009/t20200911_31864.html
         */
        for (Element elementLi : ElementsLi) {
            Elements link = elementLi.select("a");
            //
            String href = link.attr("href");
            String title = link.text();
            String date = StringUtils.strip(elementLi.select("span").text(), "()");
            System.out.println(href + ", " + title + ", " + date);
            if (date.compareTo(today) < 0) {
                break;
            }
        }
    }


    public static void main(String[] args) {

        final String htmlUrl = "https://www.12306.cn/mormhweb/1/15/index_fl.html";
        new AddedTrains().spider(htmlUrl);

    }
}
