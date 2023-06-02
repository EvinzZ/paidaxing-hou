package com.hutoto.paidaxing.service;

import cn.hutool.core.util.StrUtil;
import com.hutoto.paidaxing.commons.TypePgsqlWithJava;
import com.hutoto.paidaxing.enums.DaoOpsClientEnum;
import com.hutoto.paidaxing.enums.DevLanOpsClientEnum;
import com.hutoto.paidaxing.exception.BizException;
import com.hutoto.paidaxing.model.entity.TableField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** pgsql语句生成 */
@Service
public class PgsqlGenService {

  /**
   * ddl语句生成insert语句
   *
   * @param ddlSql ddl语句
   * @return
   */
  public String ddlGenInsertSql(String ddlSql, DaoOpsClientEnum client) throws BizException {
    String tableName = insertDdlGetTableName(ddlSql);
    StringBuilder insertSql = new StringBuilder("INSERT INTO ");
    insertSql.append(tableName).append("(");
    List<TableField> fieldList = insertDdlGetFieldNames(ddlSql);
    fieldList.forEach(o -> insertSql.append(o.getName()).append(", "));
    insertSql.deleteCharAt(insertSql.lastIndexOf(","));
    insertSql.deleteCharAt(insertSql.lastIndexOf(" "));
    insertSql.append(") values(");
    switch (client) {
      case NamedParameterJdbcTemplate:
        fieldList.forEach(
            o -> {
              if (o.getType().contains("geometry")) {
                insertSql
                    .append("ST_GeometryFromText(:")
                    .append(StrUtil.toCamelCase(o.getName()))
                    .append(", 4490)")
                    .append(", ");
              } else {
                insertSql.append(":").append(StrUtil.toCamelCase(o.getName())).append(", ");
              }
            });
        break;
      default:
        throw new BizException("该类型暂不支持");
    }
    insertSql.deleteCharAt(insertSql.lastIndexOf(","));
    insertSql.deleteCharAt(insertSql.lastIndexOf(" "));
    insertSql.append(")");
    return insertSql.toString();
  }

  /**
   * ddl -> entity
   *
   * @param ddlSql
   * @param client
   * @return
   */
  public String ddlGenEntity(String ddlSql, DevLanOpsClientEnum client) throws BizException {
    switch (client) {
      case Java:
        return ddlGenJavaEntity(ddlSql);
      default:
        throw new BizException("该类型暂不支持");
    }
  }

  /**
   * ddl生成java实体
   *
   * @param ddlSql
   * @return
   */
  public String ddlGenJavaEntity(String ddlSql) {
    String entityName = getJavaEntityName(ddlSql);
    StringBuilder entityStr = new StringBuilder();
    entityStr.append("public class ").append(entityName).append(" implements Serializable {\n");
    entityStr.append("    private static final long serialVersionUID = 1L;\n");
    List<TableField> fieldList = insertDdlGetFieldNames(ddlSql);
    fieldList.forEach(
        o ->
            entityStr
                .append("    private ")
                .append(TypePgsqlWithJava.getJavaType(o.getType()))
                .append(" ")
                .append(StrUtil.toCamelCase(o.getName()))
                .append(";\n"));
    entityStr.append("}");
    return entityStr.toString();
  }

  /**
   * 获取Java实体名
   *
   * @param ddlSql
   * @return
   */
  private static String getJavaEntityName(String ddlSql) {
    return StringUtils.capitalize(StrUtil.toCamelCase(insertDdlGetTableName(ddlSql)));
  }

  private static List<TableField> insertDdlGetFieldNames(String ddlSql) {
    List<TableField> list = new ArrayList<>();
    String[] sqlSplits = ddlSql.trim().split(";");
    String createSql = sqlSplits[0];
    String[] fieldSplits = createSql.split("\n");
    for (int i = 1; i < fieldSplits.length; i++) {
      if (fieldSplits[i].trim().startsWith("CONSTRAINT") || fieldSplits[i].startsWith(")")) {
        continue;
      }
      String[] fieldSqlSplits = fieldSplits[i].trim().split(" ");
      TableField field = new TableField();
      field.setName(fieldSqlSplits[0].replaceAll("\"", ""));
      field.setType(fieldSqlSplits[1].replaceAll("\"", "").replaceAll(",", ""));
      list.add(field);
    }
    return list;
  }

  /**
   * insert sql提取表名
   *
   * @param ddlSql
   * @return
   */
  private static String insertDdlGetTableName(String ddlSql) {
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
}
