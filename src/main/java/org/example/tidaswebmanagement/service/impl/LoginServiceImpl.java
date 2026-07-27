package org.example.tidaswebmanagement.service.impl;

import org.example.tidaswebmanagement.exception.BusinessException;
import org.example.tidaswebmanagement.mapper.LoginMapper;
import org.example.tidaswebmanagement.pojo.Emp;
import org.example.tidaswebmanagement.pojo.LoginInfo;
import org.example.tidaswebmanagement.service.LoginService;
import org.example.tidaswebmanagement.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private MailService mailService;

    @Override
    public LoginInfo login(Emp emp) {
        Emp emplog = loginMapper.login(emp.getUsername(), emp.getPassword());
        if (emplog == null) {
            return null;
        }
        // 离职员工禁止登录
        if ("离职".equals(emplog.getStatus())) {
            throw new BusinessException("您的账号已离职，无法登录系统");
        }
        LoginInfo info = new LoginInfo(emplog.getId(), emplog.getUsername(), emplog.getName());
        // role 从数据库 role_code 字段读取：admin 或 emp
        String roleCode = emplog.getRoleCode();
        info.setRole(roleCode != null ? roleCode : "emp");
        return info;
    }

    @Override
    public String register(Emp emp) {
        // 检查用户名是否已存在
        if (loginMapper.countByUsername(emp.getUsername()) > 0) {
            return "用户名已存在，请更换";
        }
        // 仅校验必填字段：用户名 + 密码（其余信息登录后可补全）
        if (emp.getUsername() == null || emp.getUsername().trim().isEmpty()) return "用户名不能为空";
        if (emp.getPassword() == null || emp.getPassword().trim().isEmpty()) return "密码不能为空";

        loginMapper.insertEmp(emp);
        return null; // null 表示成功
    }

    @Override
    public List<String> getJobs() {
        return loginMapper.getDistinctJobs();
    }

    // ===== 邮箱验证码登录 / 密码重置 =====

    @Override
    public LoginInfo loginByCode(String email, String code) {
        // 1. 校验验证码
        if (!mailService.verifyCode(email, code)) {
            throw new BusinessException("验证码错误或已过期");
        }
        // 2. 根据邮箱查用户
        Emp emp = loginMapper.findByEmail(email);
        if (emp == null) {
            throw new BusinessException("该邮箱未绑定任何账号");
        }
        // 3. 离职员工禁止登录
        if ("离职".equals(emp.getStatus())) {
            throw new BusinessException("您的账号已离职，无法登录系统");
        }
        // 4. 签发登录信息
        LoginInfo info = new LoginInfo(emp.getId(), emp.getUsername(), emp.getName());
        String roleCode = emp.getRoleCode();
        info.setRole(roleCode != null ? roleCode : "emp");
        return info;
    }

    @Override
    public String resetPassword(String email, String code, String newPassword) {
        // 1. 校验验证码
        if (!mailService.verifyCode(email, code)) {
            return "验证码错误或已过期";
        }
        // 2. 根据邮箱查用户是否存在
        Emp emp = loginMapper.findByEmail(email);
        if (emp == null) {
            return "该邮箱未绑定任何账号";
        }
        // 3. 校验新密码
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return "新密码不能为空";
        }
        if (newPassword.trim().length() < 6) {
            return "新密码长度不能少于6位";
        }
        // 4. 更新密码
        loginMapper.updatePasswordByEmail(email, newPassword.trim());
        return null; // null 表示成功
    }

    @Override
    public String findUsernameByEmail(String email) {
        Emp emp = loginMapper.findByEmail(email);
        if (emp == null || emp.getUsername() == null) {
            return null;
        }
        String username = emp.getUsername();
        // 用户名脱敏：保留首尾字符，中间用 *** 替代
        if (username.length() <= 3) {
            return username.charAt(0) + "***";
        }
        return username.charAt(0) + "***" + username.charAt(username.length() - 1);
    }
}