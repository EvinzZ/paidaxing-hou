package com.hutoto.paidaxing.commons;

import java.util.HashMap;
import java.util.Map;

public class TypePgsqlWithJava {
    private static final Map<String, String> typeMap = new HashMap<>();
    static {
        typeMap.put("VARCHAR", "String");
        typeMap.put("int 4", "Integer");
        typeMap.put("int4", "Integer");
        typeMap.put("int 2", "Integer");
        typeMap.put("int2", "Integer");
        typeMap.put("int 8", "Long");
        typeMap.put("int8", "Long");
        typeMap.put("CHAR", "String");
        typeMap.put("TIMESTAMP", "Date");
        typeMap.put("public.geometry", "String");
        typeMap.put("geometry", "String");
    }
    public static String getJavaType(String pgsqlType) {
        return typeMap.get(pgsqlType);
    }
}
