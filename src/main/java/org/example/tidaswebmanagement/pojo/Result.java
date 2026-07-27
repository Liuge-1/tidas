package org.example.tidaswebmanagement.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一返回结果（增强版）
 * code : 1=成功, 0=业务失败, -1=系统异常
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    /** 成功 */
    public static final int CODE_OK = 1;
    /** 业务失败 */
    public static final int CODE_FAIL = 0;
    /** 系统异常 */
    public static final int CODE_ERROR = -1;

    private Integer code;
    private String msg;
    private Object data;

    public Result() {}

    public Result(Integer code, String msg, Object data) {
        this.code = code; this.msg = msg; this.data = data;
    }

    // ===== 静态工厂（推荐） =====

    public static Result ok() { return new Result(CODE_OK, "success", null); }

    public static Result ok(Object data) { return new Result(CODE_OK, "success", data); }

    public static Result ok(String msg, Object data) { return new Result(CODE_OK, msg, data); }

    public static Result fail(String msg) { return new Result(CODE_FAIL, msg, null); }

    public static Result fail(int code, String msg) { return new Result(code, msg, null); }

    // ===== 向后兼容旧方法名 =====
    public static Result success() { return ok(); }
    public static Result success(Object data) { return ok(data); }
    /** @deprecated 使用 {@link #fail(String)} 代替 */
    @Deprecated
    public static Result error(String msg) { return new Result(CODE_FAIL, msg, null); }

    // ===== getter / setter =====
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
