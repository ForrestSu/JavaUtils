package com.sq.codegen.sql2mybatis;

import com.sq.utils.ReplaceUtil;
import com.sq.utils.StrUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static String FIELD_Template = "/**\n* %s\n*/"
        + "\n@Column(value = \"%s\", type = \"%s\")"
        + "\n@ApiModelProperty(\"%s\")"
        + "\nprivate %s %s;\n";


    private static void generateEntity(List<TableFields> result) {
        StringBuilder sb = new StringBuilder();
        result.forEach(f -> {
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
        });
        System.out.println(sb.toString());
    }

    private static String Audit_Template =
        "            if (before.${method}() != null) {\n" +
            "                if (!before.${method}().equals(after.${method}())) {\n" +
            "                    String change = \"${comment}从<\" + before.${method}${asenum}() + \">变更为<\" + after.${method}${asenum}() + \">\";\n" +
            "                    changList.add(change);\n" +
            "                }\n" +
            "            } else {\n" +
            "                if (after.${method}() != null) {\n" +
            "                    String change = \"补充${comment}<\" + after.${method}${asenum}() + \">\";\n" +
            "                    changList.add(change);\n" +
            "                }\n" +
            "            }\n";

    private static void genAuditLog(List<TableFields> result) {
        StringBuilder sb = new StringBuilder();
        result.forEach(f -> {
            final String getCameName = StrUtils.toCamelCase("get_" + f.getName());
            String asEnum = f.getType().equals("INT") ? "AsEnum" : "";
            Map<String, String> args = new HashMap<>();
            args.put("method", getCameName);
            args.put("comment", f.getComments());
            args.put("asenum", asEnum);
            sb.append(ReplaceUtil.resolve(Audit_Template, args));
        });
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        MysqlConnect mysql = new MysqlConnect();
        List<TableFields> result = mysql.queryTableScheme("xone_data", "t_institution");
        genAuditLog(result);
    }
}
