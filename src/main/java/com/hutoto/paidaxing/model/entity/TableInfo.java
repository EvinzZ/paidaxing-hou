package com.hutoto.paidaxing.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {
  /** 表名 */
  String name;
  /** 表备注 */
  String remark;
  /** 字段信息 */
  List<TableField> fieldList;
}
