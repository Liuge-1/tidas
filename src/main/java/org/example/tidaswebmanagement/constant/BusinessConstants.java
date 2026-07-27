package org.example.tidaswebmanagement.constant;

/** 业务常量——消除魔法值 */
public final class BusinessConstants {

    private BusinessConstants() {}

    // ===== Token =====
    public static final String TOKEN_PREFIX = "Bearer ";

    // ===== 角色 =====
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_EMP   = "emp";

    // ===== 状态 =====
    public static final String STATUS_ON  = "在职";
    public static final String STATUS_OFF = "离职";

    // ===== 操作结果 =====
    public static final String RESULT_SUCCESS = "成功";
    public static final String RESULT_FAIL    = "失败";

    // ===== 拦截器白名单 =====
    public static final String[] EXCLUDE_PATHS = {
        "/", "/index.html",
        "/login", "/register", "/send-code", "/jobs", "/refresh",
        "/login-by-code", "/reset-password", "/find-username",
        "/login.html", "/admin.html", "/employee.html",
        "/favicon.ico", "/emps/template"
    };

    // ===== 提示文本 =====
    public static final String MSG_NOT_LOGIN     = "未登录，请先登录";
    public static final String MSG_TOKEN_INVALID = "token无效或已过期";
    public static final String MSG_NO_PERMISSION = "权限不足：仅管理员可操作";
    public static final String MSG_SERVER_ERROR  = "服务器繁忙，请稍后再试";
}
