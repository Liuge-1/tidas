package org.example.tidaswebmanagement.service;

import org.example.tidaswebmanagement.pojo.Emp;
import org.example.tidaswebmanagement.pojo.LoginInfo;

import java.util.List;

public interface LoginService {
    LoginInfo login(Emp emp);

    // 注册新员工，返回 null 表示成功，返回字符串表示错误信息
    String register(Emp emp);

    // 获取已有职位列表
    List<String> getJobs();

    // ===== 邮箱验证码登录 / 密码重置 =====

    // 邮箱 + 验证码登录，返回 null 表示失败
    LoginInfo loginByCode(String email, String code);

    // 通过邮箱重置密码，返回 null 表示成功，返回字符串表示错误信息
    String resetPassword(String email, String code, String newPassword);

    // 通过邮箱找回用户名（返回脱敏后的用户名，null 表示未找到）
    String findUsernameByEmail(String email);
}
