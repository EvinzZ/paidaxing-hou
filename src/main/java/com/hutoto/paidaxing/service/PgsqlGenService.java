package com.hutoto.paidaxing.service;

import cn.hutool.core.util.StrUtil;
import com.hutoto.paidaxing.enums.DaoOpsClientEnum;
import com.hutoto.paidaxing.enums.DevLanOpsClientEnum;
import com.hutoto.paidaxing.enums.TypePgsqlWithJavaEnum;
import com.hutoto.paidaxing.exception.BizException;
import com.hutoto.paidaxing.model.entity.TableField;
import com.hutoto.paidaxing.model.entity.TableInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String tableName = createDdlGetTableInfo(ddlSql).getName();
    StringBuilder insertSql = new StringBuilder("INSERT INTO ");
    insertSql.append(tableName).append("(");
    List<TableField> fieldList = createDdlGetFieldInfo(ddlSql);
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
    String entityName = getJavaEntityName(ddlSql); // 实体名
    StringBuilder entityStr = new StringBuilder();
    entityStr.append(formatJavaEntityRemark(ddlSql));
    entityStr.append("public class ").append(entityName).append(" implements Serializable {\n");
    entityStr.append("    private static final long serialVersionUID = 1L;\n");
    List<TableField> fieldList = createDdlGetFieldInfo(ddlSql);
    fieldList.forEach(
        o ->
            entityStr
                .append(formatJavaFieldRemark(o.getRemark()))
                .append("    private ")
                .append(TypePgsqlWithJavaEnum.getJavaTypeByPgsqlType(o.getType()))
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
    return StringUtils.capitalize(StrUtil.toCamelCase(createDdlGetTableInfo(ddlSql).getName()));
  }

  /**
   * 获取Java实体名
   *
   * @param ddlSql
   * @return
   */
  private static String formatJavaEntityRemark(String ddlSql) {
    String remark = createDdlGetTableInfo(ddlSql).getRemark();
    if (remark == null) {
      return "";
    }
    return "/**\n" + " * " + remark + "\n" + " */\n";
  }

  /**
   * 获取java字段备注Str
   *
   * @param remark
   * @return
   */
  private static String formatJavaFieldRemark(String remark) {
    if (StringUtils.isBlank(remark)) {
      return "";
    }
    return "    /**\n" + "     * " + remark + "\n" + "     */\n";
  }

  private static List<TableField> createDdlGetFieldInfo(String ddlSql) {
    List<TableField> list = new ArrayList<>();
    String[] sqlSplits = ddlSql.trim().split(";");
    String createSql = sqlSplits[0];
    String[] fieldSplits = createSql.split("\n");
    Map<String, String> remark = getRemark(ddlSql);
    for (int i = 1; i < fieldSplits.length; i++) {
      if (fieldSplits[i].trim().startsWith("CONSTRAINT") || fieldSplits[i].startsWith(")")) {
        continue;
      }
      String[] fieldSqlSplits = fieldSplits[i].trim().split(" ");
      TableField field = new TableField();
      field.setName(fieldSqlSplits[0].replaceAll("\"", ""));
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
  private static Map<String, String> getRemark(String ddlSql) {
    Map<String, String> map = new HashMap<>();
    if (StringUtils.isBlank(ddlSql)) {
      return map;
    }
    String[] sqlSplits = ddlSql.trim().split(";");
    TableInfo tableInfo = createDdlGetTableInfo(ddlSql);
    for (String sqlSplit : sqlSplits) {
      if (StringUtils.isBlank(sqlSplit)) {
        continue;
      }
      if (sqlSplit.trim().startsWith("COMMENT ON COLUMN")) {
        map.put(
            StringUtils.substringBetween(sqlSplit, ".", "IS")
                .trim()
                .replaceAll("\"", "")
                .replaceAll(tableInfo.getName(), "")
                .replaceAll("\\.", ""),
            sqlSplit.trim().replaceAll("\n", "").split(" IS ")[1].trim().replaceAll("'", ""));
      }
    }
    return map;
  }

  /**
   * create sql提取表名
   *
   * @param ddlSql
   * @return
   */
  private static TableInfo createDdlGetTableInfo(String ddlSql) {
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
    String remark = null;
    for (String sqlSplit : sqlSplits) {
      if (sqlSplit.trim().startsWith("COMMENT ON")) {
        remark = sqlSplit.trim().replaceAll("\n", "").split(" IS ")[1].trim().replaceAll("'", "");
      }
    }
    TableInfo tableInfo = new TableInfo();
    tableInfo.setName(tableName);
    tableInfo.setRemark(remark);
    return tableInfo;
  }
}
