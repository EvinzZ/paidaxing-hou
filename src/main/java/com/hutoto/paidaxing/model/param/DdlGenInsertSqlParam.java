package com.hutoto.paidaxing.model.param;

import com.hutoto.paidaxing.enums.DaoOpsClientEnum;
import lombok.Data;

@Data
public class DdlGenInsertSqlParam {
  private String ddlSql;
  private DaoOpsClientEnum opsClient;
}
