package com.hutoto.paidaxing.controller;

import com.hutoto.paidaxing.commons.Result;
import com.hutoto.paidaxing.exception.BizException;
import com.hutoto.paidaxing.model.param.DdlGenEntitySqlParam;
import com.hutoto.paidaxing.model.param.DdlGenInsertSqlParam;
import com.hutoto.paidaxing.service.PgsqlGenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("pgsql")
@RestController
public class PgsqlGenController {
  @Autowired private PgsqlGenService pgsqlGenService;

  @PostMapping("ddlGenInsertSql")
  public Result ddlGenInsertSql(@RequestBody DdlGenInsertSqlParam param) {
    try {
      return Result.ok()
          .data(pgsqlGenService.ddlGenInsertSql(param.getDdlSql(), param.getOpsClient()));
    } catch (BizException v1) {
      log.error(v1.getMessage(), v1);
      return Result.error(v1.getMessage());
    } catch (Exception v2) {
      log.error(v2.getMessage(), v2);
      return Result.error();
    }
  }

  @PostMapping("ddlGenEntity")
  public Result ddlGenEntity(@RequestBody DdlGenEntitySqlParam param) {
    try {
      return Result.ok()
          .data(pgsqlGenService.ddlGenEntity(param.getDdlSql(), param.getOpsClient()));
    } catch (BizException v1) {
      log.error(v1.getMessage(), v1);
      return Result.error(v1.getMessage());
    } catch (Exception v2) {
      log.error(v2.getMessage(), v2);
      return Result.error();
    }
  }
}
