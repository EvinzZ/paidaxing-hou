package com.hutoto.paidaxing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Getter
public enum TypePgsqlWithJavaEnum {
  VARCHAR("varchar", "String", null, null),
  INT4("int4", "Integer", null, null),
  INT2("int2", "Integer", null, null),
  INT8("int8", "Long", null, null),
  CHAR("char", "String", null, null),
  TIMESTAMP("timestamp", "Date", null, null),
  GEOMETRY("geometry", "String", null, null),
  NUMERIC("numeric", "Double", null, null);

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
}
