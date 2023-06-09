package com.hutoto.paidaxing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Getter
public enum TypePgsqlWithJavaEnum {
  VARCHAR("varchar", "String", null, null),
  TEXT("text", "String", null, null),
  INT4("int4", "Integer", null, null),
  FLOAT4("float4", "Double", null, null),
  FLOAT8("float8", "Double", null, null),
  INT2("int2", "Integer", null, null),
  INT8("int8", "Long", null, null),
  CHAR("char", "String", null, null),
  TIMESTAMP(
      "timestamp",
      "Date",
      new ArrayList<String>(){{
        add("import java.util.Date;");
        add("import com.fasterxml.jackson.annotation.JsonFormat;");
        add("import org.springframework.format.annotation.DateTimeFormat;");
      }},
      new ArrayList<String>() {
        {
          add("@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
          add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+8\")");
        }
      }),
  GEOMETRY("geometry", "String", null, null),
  NUMERIC("numeric", "Double", null, null),
  DATE(
      "date",
              "Date",
              new ArrayList<String>(){{
    add("import java.util.Date;");
    add("import com.fasterxml.jackson.annotation.JsonFormat;");
    add("import org.springframework.format.annotation.DateTimeFormat;");
  }},
          new ArrayList<String>() {
    {
      add("@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
      add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+8\")");
    }
  });

  private final String pgsqlType;
  private final String javaType;
  private final List<String> imports;
  private final List<String> annotations;

  public static String getJavaTypeByPgsqlType(String pgsqlType) {
    for (TypePgsqlWithJavaEnum value : TypePgsqlWithJavaEnum.values()) {
      if (value.pgsqlType.equals(
          StringUtils.substringBefore(pgsqlType.trim().replaceAll(" ", ""), "(")
              .toLowerCase(Locale.ROOT)
              .replaceAll("public.", ""))) {
        return value.getJavaType();
      }
    }
    return null;
  }

  public static TypePgsqlWithJavaEnum getByPgsqlType(String pgsqlType) {
    for (TypePgsqlWithJavaEnum value : TypePgsqlWithJavaEnum.values()) {
      if (value.pgsqlType.equals(
              StringUtils.substringBefore(pgsqlType.trim().replaceAll(" ", ""), "(")
                      .toLowerCase(Locale.ROOT)
                      .replaceAll("public.", ""))) {
        return value;
      }
    }
    return null;
  }

  public static List<String> getImportsByPgsqlType(String pgsqlType) {
    for (TypePgsqlWithJavaEnum value : TypePgsqlWithJavaEnum.values()) {
      if (value.pgsqlType.equals(
              StringUtils.substringBefore(pgsqlType.trim().replaceAll(" ", ""), "(")
                      .toLowerCase(Locale.ROOT)
                      .replaceAll("public.", ""))) {
        return value.getImports();
      }
    }
    return null;
  }
}
