package com.hutoto.paidaxing.service;

import org.springframework.stereotype.Service;

/** mysql生成 */
@Service
public class MysqlGenService {

  public static void main(String[] args) {
    String sql = "";
    System.out.println(new MysqlGenService().ddlGenInsertSql(sql));
  }

  /**
   * ddl语句生成insert语句
   *
   * @param ddlSql ddl语句
   * @return
   */
  public String ddlGenInsertSql(String ddlSql) {

    return null;
  }
}
