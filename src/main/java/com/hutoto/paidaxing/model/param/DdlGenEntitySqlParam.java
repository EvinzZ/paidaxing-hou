package com.hutoto.paidaxing.model.param;

import com.hutoto.paidaxing.enums.DaoOpsClientEnum;
import com.hutoto.paidaxing.enums.DevLanOpsClientEnum;
import lombok.Data;

@Data
public class DdlGenEntitySqlParam {
  private String ddlSql;
  private DevLanOpsClientEnum opsClient;
}
