package com.hutoto.paidaxing.model.param;

import com.hutoto.paidaxing.enums.OpsClientEnum;
import lombok.Data;

@Data
public class DdlGenInsertSqlParam {
  private String ddlSql;
  private OpsClientEnum opsClient;
}
