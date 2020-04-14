package com.sq.sql.sql2mybatis;

public class TypeConvert {
    /**
    case "CHAR":
    case "VARCHAR":
    case "LONGVARCHAR":
    case "TEXT":
    case "BIT":
    case "BOOLEAN":
    case "INTEGER":
    case "BIGINT":
    case "DECIMAL":
    case "NUMERIC":
    case "REAL":
    case "FLOAT":
    case "DOUBLE":
        return sqlType;
    */
    public static String getSwaggerType(String sqlType) {
        sqlType = sqlType.toUpperCase();
        if ("INT".equals(sqlType)) {
            return "INTEGER";
        }
        return sqlType;
    }

    public static String getJDBCType(String sqlType) {
        sqlType = sqlType.toUpperCase();
        if ("INT".equals(sqlType)) {
            return "INTEGER";
        }
        return sqlType;
    }

    public static String getJavaType(String sqlType) {
        if (sqlType == null || sqlType.isEmpty()) {
            System.err.println("sqlType is null!");
            return "";
        }
        sqlType = sqlType.toUpperCase();
        switch (sqlType) {
            case "CHAR":
            case "VARCHAR":
            case "LONGVARCHAR":
            case "TEXT":
                return "String";
            case "BIT":
            case "BOOLEAN":
                return "Boolean";
            case "INT":
            case "INTEGER":
                return "Integer";
            case "BIGINT":
                return "Long";
            case "DECIMAL":
            case "NUMERIC":
                return "BigDecimal";
            case "REAL":
                return "Float";
            case "FLOAT":
            case "DOUBLE":
                return "Double";
            default:
                System.err.println("can't match type:" + sqlType);
                return "";
        }
    }
}
