package com.hutoto.paidaxing.controller;

import com.hutoto.paidaxing.commons.Result;
import com.hutoto.paidaxing.enums.DaoOpsClientEnum;
import com.hutoto.paidaxing.enums.DevLanOpsClientEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 字典 */
@RestController
@RequestMapping("dict")
public class DictController {

  @GetMapping("listDaoOpsClient")
  public Result listDaoOpsClient() {
    return Result.ok().data(DaoOpsClientEnum.values());
  }

  @GetMapping("listDevLanOpsClient")
  public Result listDevLanOpsClient() {
    return Result.ok().data(DevLanOpsClientEnum.values());
  }
}
