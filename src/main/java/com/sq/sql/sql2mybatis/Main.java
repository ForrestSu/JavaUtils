package com.sq.sql.sql2mybatis;

import com.sunquan.utils.StrUtils;

import java.util.List;

public class Main {
    private static String FIELD_Template = "/**\n* %s\n*/"
            + "\n@Column(value = \"%s\", type = \"%s\")"
            + "\n@ApiModelProperty(\"%s\")"
            + "\nprivate %s %s;\n";

    private static  void GenerateEntity(List<TableFields> result){
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

    private static String XML_Template = "\n<result column=\"%s\" property=\"%s\" jdbcType=\"%s\"/>";
    private static  void GenerateXML(List<TableFields> result){
        StringBuilder sb = new StringBuilder();
        for (TableFields f : result) {
            String cameName = StrUtils.toCamelCase(f.getName());
            String jdbcType = TypeConvert.getJDBCType(f.getType());
            final String text = String.format(XML_Template,
                    f.getName(),
                    cameName,
                    jdbcType
                    );
            sb.append(text);
        }
        System.out.println(sb.toString());
    }


    private static String COLS_Template = ",\n`t`.`%s`";
    private static  void GenerateAllCol(List<TableFields> result){
        StringBuilder sb = new StringBuilder();
        for (TableFields f : result) {
            final String text = String.format(COLS_Template,
                    f.getName()
            );
            sb.append(text);
        }
        System.out.println(sb.toString());
    }

    private static void genMybatis() {
        MysqlConnect mysql = new MysqlConnect();
        List<TableFields> result = mysql.QueryTableScheme("xone_data", "t_institution_plan");
        GenerateEntity(result);
        GenerateXML(result);
        GenerateAllCol(result);
    }

    private static String PlusFields = "/**\n* %s\n*/"
            + "\n@TableField(\"%s\")"
            + "\n@ApiModelProperty(\"%s\")"
            + "\nprivate %s %s;\n";
    private static  void ImpMybatisPlus(List<TableFields> result){
        StringBuilder sb = new StringBuilder();
        for (TableFields f : result) {
            String javaType = TypeConvert.getJavaType(f.getType());
            String cameFieldName = StrUtils.toCamelCase(f.getName());
            final String text = String.format(PlusFields,
                    f.getComments(),
                    f.getName(),
                    f.getComments(),
                    javaType,
                    cameFieldName);
            sb.append(text);
        }
        System.out.println(sb.toString());
    }
    private static void genMybatisPlus() {
        MysqlConnect mysql = new MysqlConnect();
        List<TableFields> result = mysql.QueryTableScheme("xone_data", "t_institution_plan");
        ImpMybatisPlus(result);
    }

    public static void main(String[] args) {
        genMybatisPlus();
    }
}
