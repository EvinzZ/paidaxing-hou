package com.hutoto.paidaxing.model.param;

import com.hutoto.paidaxing.enums.DaoOpsClientEnum;
import lombok.Data;

/** 生产update语句参数 */
@Data
public class DdlGenUpdateSqlParam {
  private String ddlSql;
  private DaoOpsClientEnum opsClient;
}
