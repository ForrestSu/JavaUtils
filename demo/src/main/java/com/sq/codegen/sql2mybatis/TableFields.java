package com.sq.codegen.sql2mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class TableFields {
    /**
     * 字段名
     */
    private String name;
    private String type;
    private String comments;
}
