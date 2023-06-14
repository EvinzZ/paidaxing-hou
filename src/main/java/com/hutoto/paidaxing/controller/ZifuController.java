package com.hutoto.paidaxing.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.hutoto.paidaxing.commons.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/** 字符处理接口 */
@Slf4j
@RestController
@RequestMapping("zifu")
public class ZifuController {

  /**
   * 驼峰转下滑
   *
   * @param text
   * @param upperCase
   * @return
   */
  @GetMapping("toUnderlineCase")
  public Result toUnderlineCase(@RequestParam String text, @RequestParam Boolean upperCase) {
    if (upperCase) return Result.ok().data(StrUtil.toUnderlineCase(text).toUpperCase(Locale.ROOT));
    else return Result.ok().data(StrUtil.toUnderlineCase(text));
  }

  /**
   * 下划线转驼峰
   *
   * @param text
   * @return
   */
  @GetMapping("toCamelCase")
  public Result toCamelCase(@RequestParam String text) {
    return Result.ok().data(StrUtil.toCamelCase(text));
  }

  /**
   * 获取拼音首字母
   *
   * @param text 内容
   * @param separator 拼接符
   * @return
   */
  @GetMapping("getFirstLetter")
  public Result getFirstLetter(@RequestParam String text, String separator) {
    try {
      if (separator == null) {
        separator = StringUtils.EMPTY;
      }
      return Result.ok().data(PinyinUtil.getFirstLetter(text, separator));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.error();
    }
  }

  /**
   * 获取拼音
   *
   * @param text 内容
   * @param separator 拼接符
   * @return
   */
  @GetMapping("getPinyin")
  public Result getPinyin(@RequestParam String text, String separator) {
    try {
      if (separator == null) {
        separator = StringUtils.EMPTY;
      }
      return Result.ok().data(PinyinUtil.getPinyin(text, separator));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.error();
    }
  }
}
