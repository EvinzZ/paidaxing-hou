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
import java.util.Random;

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
    StringBuilder entityStr = new StringBuilder();
    TableInfo tableInfo = CreateDdlSqlUtils.ddlGetTableInfo(ddlSql);
    writeImports(tableInfo, entityStr);
    writeJavaEntityRemark(tableInfo, entityStr);
    writeJavaEntityAnnotations(entityStr);
    writeJavaEntityName(tableInfo, entityStr);
    writeJavaEntitySerialUid(entityStr);
    writeJavaEntityField(tableInfo.getFieldList(), entityStr);
    writeEnd(entityStr);
    return entityStr.toString();
  }

  /**
   * 拼接java实体字段
   *
   * @param fieldList 字段列表
   * @param entityStr 待拼写字符串
   */
  private static void writeJavaEntityField(List<TableField> fieldList, StringBuilder entityStr) {
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
  }

  private void writeJavaEntityAnnotations(StringBuilder appendStr) {
    if (this.useLombok.get()) {
      appendStr.append("@Data\n" + "@NoArgsConstructor\n" + "@AllArgsConstructor\n");
    }
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
  public void writeJavaEntityName(TableInfo tableInfo, StringBuilder appendStr) {
    appendStr
        .append("public class ")
        .append(getJavaEntityName(tableInfo))
        .append(" implements Serializable {\n");
  }

  private void writeJavaEntitySerialUid(StringBuilder appendStr) {
    appendStr.append("\tprivate static final long serialVersionUID = 1L;");
  }

  /**
   * 拼接import声明包
   *
   * @param tableInfo 表信息
   * @param appendStr 主字符串
   */
  private void writeImports(TableInfo tableInfo, StringBuilder appendStr) {
    getFieldJavaEntityImports(tableInfo.getFieldList())
        .forEach(o -> appendStr.append(o).append("\n"));
    appendStr.append("\n");
  }

  private void writeEnd(StringBuilder appendStr) {
    appendStr.append("\n}");
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
   * 拼接java备注
   *
   * @param tableInfo
   * @return
   */
  private static void writeJavaEntityRemark(TableInfo tableInfo, StringBuilder appendStr) {
    String remark = tableInfo.getRemark();
    if (remark == null) {
      return;
    }
    appendStr.append("/**\n" + " * " + remark + "\n" + " */\n");
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

  public static void main(String[] args) {
    for (int i = 0; i < 152; i++) {
      int max = 100000;
      int min = 10000;
      Random random = new Random();

      int s = random.nextInt(max) % (max - min + 1) + min;

      System.out.println("40.8" + s);
    }
  }
}
