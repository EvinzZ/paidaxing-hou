package com.hutoto.paidaxing.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回体
 *
 * @author
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  // 状态码
  private Integer code;

  // 响应消息
  private String message;

  // 响应数据
  private Object data;

  // 报错信息
  private Exception exception;

  // 报错信息
  private String exceptionMsg;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public static Result ok() {
    Result result = new Result();
    result.setCode(ResultCode.SUCCESS.getCode());
    result.setMessage(ResultCode.SUCCESS.getMessage());
    return result;
  }

  public static Result ok(String msg) {
    Result result = new Result();
    result.setCode(ResultCode.SUCCESS.getCode());
    result.setMessage(msg);
    return result;
  }

  public Result data(Object data) {
    this.setData(data);
    return this;
  }

  public Result exception(Exception e) {
    this.setException(e);
    this.setExceptionMsg(e.getMessage());
    return this;
  }

  public static Result error() {
    Result result = new Result();
    result.setCode(ResultCode.ERROR.getCode());
    result.setMessage(ResultCode.ERROR.getMessage());
    return result;
  }

  public static Result error(String msg) {
    Result result = new Result();
    result.setCode(ResultCode.ERROR.getCode());
    result.setMessage(msg);
    return result;
  }

  public static Result custom(Integer code, String msg) {
    Result result = new Result();
    result.setCode(code);
    result.setMessage(msg);
    return result;
  }

  public static Result custom(ResultCode resultCode) {
    Result result = new Result();
    result.setCode(resultCode.getCode());
    result.setMessage(resultCode.getMessage());
    return result;
  }

  public Result(ResultCode resultCode, Object data) {
    this.code = resultCode.getCode();
    this.message = resultCode.getMessage();
    this.data = data;
  }

  public Result(Integer code, String msg, Object data) {
    this.code = code;
    this.message = msg;
    this.data = data;
  }

  public Result(ResultCode resultCode) {
    this.code = resultCode.getCode();
    this.message = resultCode.getMessage();
  }

  public Result(Integer code, String msg) {
    this.code = code;
    this.message = msg;
  }
}
