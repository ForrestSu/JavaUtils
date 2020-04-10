package com.sq.sql.sql2mybatis;

import com.sunquan.utils.StrUtils;

import java.util.List;

public class Main {
    private static String FIELD_Template = "/**\n* %s\n*/"
            + "\n@Column(value = \"%s\", type = \"%s\")"
            + "\n@ApiModelProperty(\"%s\")"
            + "\nprivate %s %s;\n";

    private static void Work() {
        MysqlConnect mysql = new MysqlConnect();
        List<TableFields> result = mysql.QueryTableScheme("xone_data", "t_custom_product");

        StringBuilder sb = new StringBuilder();
        for (TableFields f : result) {

            String swaggerType = TypeConvert.getSwaggerType(f.getType());
            String javaType = TypeConvert.getJavaType(f.getType());
            String cameName = StrUtils.toCamelCase(f.getName());
            final String text = String.format(FIELD_Template,
                    f.getComments(),
                    f.getName(),
                    swaggerType,
                    f.getComments(),
                    javaType,
                    cameName);
            sb.append(text);
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        Work();
    }
}
