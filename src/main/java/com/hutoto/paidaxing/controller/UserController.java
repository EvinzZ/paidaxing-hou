package com.hutoto.paidaxing.controller;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

  @PostMapping("login")
  public Map<String, Object> login(@RequestBody Map<String, Object> param) {
    //
    log.info("user -> {}", param);
    Map<String, Object> res = new HashMap<>();
    res.put("token", IdUtil.simpleUUID());
    return res;
  }

  @GetMapping("captchaImage")
  public Map<String, Object> captchaImage() {
    //
    return null;
  }

  @GetMapping("getInfo")
  public Map<String, Object> getUserInfo() {
    Map<String, Object> user = new HashMap<>();
    user.put("userName", "admin");
    Map<String, Object> map = new HashMap<>();
    map.put("user", user);
    return map;
  }

  @GetMapping("getRouters")
  public Map<String, Object> getRouters() {
    List<Map<String, Object>> list = new ArrayList<>();
    list.add(
        new HashMap<String, Object>() {
          {
            put("path", "/mysql");
          }
        });
    list.add(
        new HashMap<String, Object>() {
          {
            put("path", "/pgsql");
          }
        });
    Map<String, Object> map = new HashMap<>();
    map.put("data", list);
    return map;
  }
}
