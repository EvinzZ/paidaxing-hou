package com.hutoto.paidaxing.service;

import cn.hutool.core.util.StrUtil;
import com.hutoto.paidaxing.enums.DaoOpsClientEnum;
import com.hutoto.paidaxing.enums.DevLanOpsClientEnum;
import com.hutoto.paidaxing.enums.TypePgsqlWithJavaEnum;
import com.hutoto.paidaxing.exception.BizException;
import com.hutoto.paidaxing.model.entity.TableField;
import com.hutoto.paidaxing.model.entity.TableInfo;
import com.hutoto.paidaxing.util.CreateDdlSqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** pgsql语句生成 */
@Service
public class PgsqlGenService {
  ThreadLocal<Boolean> useLombok = ThreadLocal.withInitial(() -> true);

  /**
   * ddl语句生成insert语句
   *
   * @param ddlSql ddl语句
   * @return
   */
  public String ddlGenInsertSql(String ddlSql, DaoOpsClientEnum client) throws BizException {
    TableInfo tableInfo = CreateDdlSqlUtils.ddlGetTableInfo(ddlSql);
    String tableName = tableInfo.getName();
    StringBuilder insertSql = new StringBuilder("INSERT INTO ");
    insertSql.append(tableName).append("(");
    List<TableField> fieldList = tableInfo.getFieldList();
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
   * @param useLombok
   * @return
   */
  public String ddlGenEntity(String ddlSql, DevLanOpsClientEnum client, boolean useLombok)
      throws BizException {
    this.useLombok.set(useLombok);
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
    TableInfo tableInfo = CreateDdlSqlUtils.ddlGetTableInfo(ddlSql);
    StringBuilder entityStr = ddlGenJavaEntityInit(tableInfo);
    List<TableField> fieldList = tableInfo.getFieldList();
    for (TableField o : fieldList) {
      TypePgsqlWithJavaEnum pgsqlType = TypePgsqlWithJavaEnum.getByPgsqlType(o.getType());
      entityStr
          .append(getFieldAnnotationAppend(pgsqlType))
          .append(formatJavaFieldRemark(o.getRemark()))
          .append("\tprivate ")
          .append(pgsqlType == null ? null : pgsqlType.getJavaType())
          .append(" ")
          .append(StrUtil.toCamelCase(o.getName()))
          .append(";");
    }
    entityStr.append("\n}");
    return entityStr.toString();
  }

  private static String getFieldAnnotationAppend(TypePgsqlWithJavaEnum pgsqlType) {
    return pgsqlType == null
        ? ""
        : (pgsqlType.getAnnotations() == null
                ? ""
                : "\n\t" + StringUtils.join(pgsqlType.getAnnotations(), "\n\t"))
            + "\n";
  }

  /**
   * 初始化java实体
   *
   * @param tableInfo
   * @return
   */
  public StringBuilder ddlGenJavaEntityInit(TableInfo tableInfo) {
    String entityName = getJavaEntityName(tableInfo); // 实体名
    StringBuilder entityStr = new StringBuilder();
    getFieldJavaEntityImports(tableInfo.getFieldList())
        .forEach(o -> entityStr.append(o).append("\n"));
    entityStr.append("\n");
    entityStr.append(formatJavaEntityRemark(tableInfo));
    if (this.useLombok.get()) {
      entityStr.append("@Data\n" + "@NoArgsConstructor\n" + "@AllArgsConstructor\n");
    }
    entityStr.append("public class ").append(entityName).append(" implements Serializable {\n");
    entityStr.append("\tprivate static final long serialVersionUID = 1L;");
    return entityStr;
  }

  /**
   * 获取java导入
   *
   * @param fieldList
   * @return
   */
  private List<String> getFieldJavaEntityImports(List<TableField> fieldList) {
    List<String> list = new ArrayList<>();
    list.add("import java.io.Serializable;");
    if (this.useLombok.get()) {
      list.add("import lombok.AllArgsConstructor;");
      list.add("import lombok.Data;");
      list.add("import lombok.NoArgsConstructor;");
    }
    for (TableField field : fieldList) {
      List<String> importsByPgsqlType =
          TypePgsqlWithJavaEnum.getImportsByPgsqlType(field.getType());
      if (importsByPgsqlType == null) {
        continue;
      }
      list.addAll(importsByPgsqlType);
    }
    list = removeDuplicates(list);
    return sort(list);
  }

  /**
   * 去重
   *
   * @param list
   * @param <T>
   * @return
   */
  private static <T> List<T> removeDuplicates(List<T> list) {
    return new ArrayList<>(new LinkedHashSet<>(list));
  }

  private static List<String> sort(List<String> list) {
    list.sort(String::compareTo);
    return list;
  }

  /**
   * 获取Java实体名
   *
   * @param tableInfo
   * @return
   */
  private static String getJavaEntityName(TableInfo tableInfo) {
    return StringUtils.capitalize(StrUtil.toCamelCase(tableInfo.getName()));
  }

  /**
   * 获取Java实体名
   *
   * @param tableInfo
   * @return
   */
  private static String formatJavaEntityRemark(TableInfo tableInfo) {
    String remark = tableInfo.getRemark();
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
    return "\t/**\n" + "\t * " + remark + "\n" + "\t */\n";
  }
}
