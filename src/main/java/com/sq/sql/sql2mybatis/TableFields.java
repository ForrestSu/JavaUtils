package com.sq.sql.sql2mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class TableFields {
    private String name;
    private String type;
    private String comments;
}
