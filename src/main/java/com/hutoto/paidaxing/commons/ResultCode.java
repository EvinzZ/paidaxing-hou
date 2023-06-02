package com.hutoto.paidaxing.commons;

public enum ResultCode {

  // 成功
  SUCCESS(200, "成功"),
  PARAM_IS_INVALID(1001, "参数无效"),
  PARAM_IS_BLANK(1002, "参数为空"),
  PARAM_IS_ERROR(1004, "参数错误"),
  PARAM_IS_COMPLETE(1003, "参数缺失"),
  USER_LONGIN_ERROR(2001, "账号或密码错误"),
  USER_LONGIN_EXIST(2002, "用户不存在"),
  USER_LONGIN_EXISTD(2003, "用户已存在"),
  KHXX_EXISTD(2010, "客户信息已存在！"),
  USER_LONGIN_EXAMINE(2004, "用户待审核"),
  UPDATE_EXAMINE(2006, "修改申请已提交，等待管理员审核"),
  USER_LONGIN_STOP(2005, "用户已停用"),
  ERROR(500, "服务器开小差了"),
  CUSTOMER_EXISTD(400, "客户信息已存在"),
  PHONE_ERROR(402, "验证码或者手机号错误"),
  PHONE_EXIST(405, "手机号为空"),
  PHONE_RETRY(405, "请2分钟后重试"),
  PHONE_FPRMATEXIST(406, "手机号错误"),
  SINE_ERROR(401, "用户信息已过期，请重新登录"),
  LONG_OVERTIME(406, "登录超时，请重新登录"),
  KHXX_HTJH_EXISTD(1005, "该类型的计划已存在"),
  USER_INSUFFICIENT_AUTHORITY(403, "权限不足"),
  FILE_NOTBLANK(2007, "上传文件不能为空"),
  FILE_MAXSIZE(2008, "上传文件不能超过2M"),
  FILE_ERRORSUFFIX(2009, "上传文件错误，只能上传文档或表格以及PDF格式"),
  DASHANG_ZIJI(1004, "不能给自己打赏"),
  YU_E_BUZU(1005, "余额不足"),

  SIGN_IN_EXISTD(2020, "今日已签到，无需重复签到"),

  /** 失败 */
  FAIL(400, "请求失败"),

  /** 登录 */
  ACCOUNT_IS_EMPTY(10001, "账号不能为空"),
  ACCOUNT_OR_PASSWORD_ERROR(10002, "账号或密码错误"),
  ACCOUNT_NAME_EXITS(10003, "账号已存在"),
  ACCOUNT_DISABLED(10004, "账号已禁用"),
  USER_INFO_NOT_EXIST(10005, "用户信息不存在"),
  USER_INFO_UPDATE_FAIL(10006, "更新用户信息失败"),
  USER_INFO_SAVE_FAIL(10007, "保存用户信息失败"),

  PHONE_NUMBER_IS_EMPTY(10102, "手机号不能为空"),
  PHONE_IS_EXITS_BIND(10103, "手机号已绑定"),
  PHONE_EXITS_WITH_CURRENT(10104, "手机号与当前一致"),
  PHONE_RESET_FAIL(10105, "变更手机号码失败"),
  PHONE_EXITS(10106, "手机号已存在"),
  PHONE_NOT_EXITS(10107, "手机号不存在"),

  PASSWORD_IS_EMPTY(10201, "密码不能为空"),
  PASSWORD_ERROR(10202, "两次密码不一致"),

  SMS_CODE_ERROR(10301, "验证码不正确"),
  SMS_CODE_COUNT(10302, "验证码超过发送次数"),
  SMS_TYPE_ERROR(10303, "短信类型不正确"),
  SMS_CODE_IS_EMPTY(10304, "验证码不能为空"),

  LOGIN_FAIL(20001, "登录失败"),
  UN_LOGIN(20002, "未登录，请先登录"),
  LOGIN_METHOD_IS_EMPTY(20004, "登录方式不能为空"),
  AUTH_SERVER_ERROR(20005, "授权服务异常"),

  REGISTER_SUCCESS(20101, "注册成功"),
  REGISTER_FAIL(20102, "注册失败"),

  /** 文件上传 */
  FILE_NOT_EMPTY(30001, "文件不能为空"),
  FILE_NOT_SUPPORT(30002, "文件不支持"),
  FILE_TOO_LARGE(30003, "文件太大"),
  FILE_LEGITIMATE_ERROR(30004, "文件不合法"),
  FILE_UPLOAD_ERROR(30005, "文件上传失败"),

  UPLOAD_ATTACHMENT_SIZE_ERROR(30100, "上传的文件超过6个"),
  UPLOAD_ATTACHMENT_RECORD_NOT_EXIST(30101, "文件记录不存在"),

  /** Excel 导出失败 */
  EXCEL_IMPORT_FAIL(40001, "excel 导入失败"),
  EXCEL_EXPORT_FAIL(40002, "excel 导出失败"),

  /** permission */
  PERMISSION_ACCESS_DENIED(50001, "permission access denied"),

  /** 操作频繁 */
  OPERATION_LIMIT_FAIL(50101, "访问频繁，请稍后再试"),

  THIRD_PART_LOGIN_FAIL(60001, "第三方授权登录失败"),
  THIRD_PART_CODE_EMPTY(60002, "第三方授权码不能为空"),

  QR_CODE_TICKET_EMPTY(70001, "二维码扫描票据不存在"),

  /** 参数校验 */
  PARAMS_VALIDATE_FAIL(-10000, "参数校验失败"),
  PARAMS_REQUEST_VALIDATE_FAIL(-10002, "请求参数错误"),

  /** token */
  TOKEN_IS_EMPTY(-10101, "token must not be null"),
  TOKEN_VALIDATE_FAIL(-10102, "token check fail"),
  TOKEN_INVALID(-10103, "invalid access token"),
  REFRESH_TOKEN_NOT_EXIST(-10104, "refresh token not exist"),
  ACCESS_TOKEN_IS_EMPTY(-10105, "access token is empty"),

  /** 未知错误 */
  UN_KNOWN_ERROR(-1, "未知错误"),
  ;

  private Integer code;

  private String message;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  ResultCode(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
}
