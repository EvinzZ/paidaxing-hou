package com.hutoto.paidaxing.util;

import com.hutoto.paidaxing.model.entity.TableField;
import com.hutoto.paidaxing.model.entity.TableInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateDdlSqlUtils {
  private static List<TableField> ddlGetFieldInfo(String ddlSql) {
    List<TableField> list = new ArrayList<>();
    String[] sqlSplits = ddlSql.trim().split(";");
    String createSql = sqlSplits[0];
    String[] fieldSplits = createSql.split("\n");
    Map<String, String> remark = ddlGetFieldRemark(ddlSql);
    for (int i = 1; i < fieldSplits.length; i++) {
      if (fieldSplits[i].trim().startsWith("CONSTRAINT") || fieldSplits[i].startsWith(")")) {
        continue;
      }
      String[] fieldSqlSplits = fieldSplits[i].trim().split(" ");
      TableField field = new TableField();
      field.setName(fieldSqlSplits[0].replaceAll("\"", "").trim());
      field.setType(fieldSqlSplits[1].replaceAll("\"", "").replaceAll(",", ""));
      field.setRemark(remark.get(field.getName()));
      list.add(field);
    }
    return list;
  }

  /**
   * 获取备注
   *
   * @param ddlSql
   * @return
   */
  private static Map<String, String> ddlGetFieldRemark(String ddlSql) {
    Map<String, String> map = new HashMap<>();
    if (StringUtils.isBlank(ddlSql)) {
      return map;
    }
    String[] sqlSplits = ddlSql.trim().split(";");
    for (String sqlSplit : sqlSplits) {
      if (StringUtils.isBlank(sqlSplit)) {
        continue;
      }
      if (sqlSplit.trim().startsWith("COMMENT ON COLUMN")) {
        map.put(
            StringUtils.substringBetween(sqlSplit, ".", "IS")
                .trim()
                .replaceAll("\"", "")
                .replaceAll(ddlGetTableName(ddlSql), "")
                .replaceAll("\\.", ""),
            sqlSplit.trim().replaceAll("\n", "").split(" IS ")[1].trim().replaceAll("'", ""));
      }
    }
    return map;
  }

  public static String ddlGetTableName(String ddlSql) {
    String[] sqlSplits = ddlSql.trim().split(";");
    String createSql = sqlSplits[0];
    String[] fieldSplits = createSql.split("\n");
    String[] tableNameSplits = fieldSplits[0].split("\\(");
    String tableCreatePrefix = tableNameSplits[0].trim();
    String tableName;
    if (tableCreatePrefix.contains("\"public\".")) {
      tableName =
          StringUtils.remove(
              StringUtils.removeStart(tableCreatePrefix, "CREATE TABLE \"public\"."), "\"");
    } else {
      tableName =
          StringUtils.remove(StringUtils.removeStart(tableCreatePrefix, "CREATE TABLE "), "\"");
    }
    return tableName;
  }

  /**
   * create sql提取表名
   *
   * @param ddlSql
   * @return
   */
  public static TableInfo ddlGetTableInfo(String ddlSql) {
    TableInfo tableInfo = new TableInfo();
    tableInfo.setName(ddlGetTableName(ddlSql));
    tableInfo.setRemark(getTableRemark(ddlSql));
    tableInfo.setFieldList(ddlGetFieldInfo(ddlSql));
    return tableInfo;
  }

  private static String getTableRemark(String ddlSql) {
    String[] sqlSplits = ddlSql.trim().split(";");
    String remark = null;
    for (String sqlSplit : sqlSplits) {
      if (sqlSplit.trim().startsWith("COMMENT ON TABLE")) {
        System.out.println(sqlSplit.trim());
        remark =
            sqlSplit
                .trim()
                .replaceAll("\r", "")
                .replaceAll("\n", "")
                .split(" IS ")[1]
                .trim()
                .replaceAll("'", "");
      }
    }
    return remark;
  }
}
