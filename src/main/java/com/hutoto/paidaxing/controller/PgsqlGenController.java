package com.hutoto.paidaxing.controller;

import com.hutoto.paidaxing.commons.Result;
import com.hutoto.paidaxing.commons.ResultCode;
import com.hutoto.paidaxing.exception.BizException;
import com.hutoto.paidaxing.model.param.DdlGenEntitySqlParam;
import com.hutoto.paidaxing.model.param.DdlGenInsertSqlParam;
import com.hutoto.paidaxing.model.param.DdlGenUpdateSqlParam;
import com.hutoto.paidaxing.service.PgsqlGenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
      if (StringUtils.isBlank(param.getDdlSql())) {
        return Result.custom(ResultCode.PARAM_IS_BLANK);
      }
      if (param.getOpsClient() == null) {
        return Result.custom(ResultCode.PARAM_IS_BLANK);
      }
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
      if (StringUtils.isBlank(param.getDdlSql())) {
        return Result.custom(ResultCode.PARAM_IS_BLANK);
      }
      if (param.getOpsClient() == null) {
        return Result.custom(ResultCode.PARAM_IS_BLANK);
      }
      return Result.ok()
          .data(
              pgsqlGenService.ddlGenEntity(
                  param.getDdlSql(), param.getOpsClient(), param.isUseLombok()));
    } catch (BizException v1) {
      log.error(v1.getMessage(), v1);
      return Result.error(v1.getMessage());
    } catch (Exception v2) {
      log.error(v2.getMessage(), v2);
      return Result.error();
    }
  }

  /**
   * 生成update by id语句
   *
   * @param param
   * @return
   * @throws BizException
   */
  @PostMapping("ddlGenUpdateSql")
  public Result ddlGenUpdateSql(@RequestBody DdlGenUpdateSqlParam param) throws BizException {
    if (StringUtils.isBlank(param.getDdlSql())) {
      return Result.custom(ResultCode.PARAM_IS_BLANK);
    }
    if (param.getOpsClient() == null) {
      return Result.custom(ResultCode.PARAM_IS_BLANK);
    }
    return Result.ok()
        .data(
            pgsqlGenService.ddlGenUpdateSql(
                param.getDdlSql(), param.getOpsClient()));
  }
}
