package org.example.tidaswebmanagement.utils;

public class UserContext {
    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();

    // 存入当前登录用户id
    public static void setUserId(Integer id) {
        USER_ID.set(id);
    }

    // 获取当前登录用户id
    public static Integer getUserId() {
        return USER_ID.get();
    }

    // 存入当前登录用户角色
    public static void setRole(String role) {
        ROLE.set(role);
    }

    // 获取当前登录用户角色
    public static String getRole() {
        return ROLE.get();
    }

    // 必须清理，防止内存泄漏
    public static void remove() {
        USER_ID.remove();
        ROLE.remove();
    }
}