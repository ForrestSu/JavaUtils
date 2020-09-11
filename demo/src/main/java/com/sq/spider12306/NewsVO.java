package com.sq.spider12306;

import lombok.Data;

@Data
public class NewsVO {

    /**
     * 超链接
     */
    private String links;
    /**
     * 标题
     */
    private String title;

    /**
     * 发布日期: 2020-09-11
     */
    private String publishDate;
}
