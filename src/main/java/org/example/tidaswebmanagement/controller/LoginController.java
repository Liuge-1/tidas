package org.example.tidaswebmanagement.controller;


import jakarta.validation.Valid;
import org.example.tidaswebmanagement.anno.RateLimit;
import org.example.tidaswebmanagement.dto.EmpRegisterDTO;
import org.example.tidaswebmanagement.exception.BusinessException;
import org.example.tidaswebmanagement.pojo.Emp;
import org.example.tidaswebmanagement.pojo.LoginInfo;
import org.example.tidaswebmanagement.pojo.Result;
import org.example.tidaswebmanagement.service.LoginService;
import org.example.tidaswebmanagement.service.MailService;
import org.example.tidaswebmanagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MailService mailService;

    @RateLimit(10)  // 登录接口限流：每分钟最多10次，防暴力破解
    @PostMapping ("/login")//emp接收用户名和密码
    public Result login(@RequestBody Emp emp) {
        Logger logger = Logger.getLogger(LoginController.class.getName());
        logger.info("登录用户名：" + emp.getUsername());

        LoginInfo loginInfo;
        try {
            loginInfo = loginService.login(emp);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }

        if(loginInfo == null) {
            return Result.fail("用户名或密码错误");
        }

            //登陆成功，生成token（包含角色信息）
        String token = jwtUtil.generateToken(loginInfo.getId(), loginInfo.getRole());
        loginInfo.setToken(token);

        // 生成 refreshToken（7天有效期）
        String refreshToken = jwtUtil.generateRefreshToken(loginInfo.getId(), loginInfo.getRole());
        loginInfo.setRefreshToken(refreshToken);


        return Result.success(loginInfo);


    }

    // ===== Token 刷新接口（使用 refreshToken 换取新 accessToken） =====
    @PostMapping("/refresh")
    public Result refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Result.fail("refreshToken不能为空");
        }
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            return Result.fail("refreshToken无效或已过期");
        }
        Integer userId = jwtUtil.getUserIdByToken(refreshToken);
        String role = jwtUtil.getRoleByToken(refreshToken);
        String newToken = jwtUtil.generateToken(userId, role);

        Map<String, String> data = new HashMap<>();
        data.put("token", newToken);
        return Result.success(data);
    }

    // ===== 发送验证码（无需登录，支持 register/login/reset-password） =====
    @PostMapping("/send-code")
    public Result sendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String purpose = body.get("purpose"); // register / login / reset-password
        if (email == null || email.trim().isEmpty()) {
            return Result.fail("邮箱不能为空");
        }
        String error = mailService.sendCode(email.trim(), purpose);
        if (error != null) {
            return Result.fail(error);
        }
        return Result.success("验证码已发送至 " + email + "，5分钟内有效");
    }

    // ===== 注册（无需登录） =====
    @PostMapping("/register")
    public Result register(@Valid @RequestBody EmpRegisterDTO dto) {
        Logger logger = Logger.getLogger(LoginController.class.getName());

        // 校验验证码
        if (!mailService.verifyCode(dto.getEmail().trim(), dto.getCode().trim())) {
            throw new BusinessException("验证码错误或已过期");
        }

        Emp emp = new Emp();
        emp.setUsername(dto.getUsername());
        emp.setPassword(dto.getPassword());
        emp.setEmail(dto.getEmail().trim());
        emp.setName(dto.getName());
        emp.setGender(dto.getGender());
        emp.setPhone(dto.getPhone());
        emp.setJob(dto.getJob());

        logger.info("注册请求：" + emp.getUsername() + "，邮箱：" + emp.getEmail());

        String error = loginService.register(emp);
        if (error != null) {
            throw new BusinessException(error);
        }
        logger.info("注册成功，用户id：" + emp.getId());
        return Result.ok("注册成功");
    }

    // ===== 获取已有职位列表（无需登录） =====
    @GetMapping("/jobs")
    public Result getJobs() {
        List<String> jobs = loginService.getJobs();
        return Result.success(jobs);
    }

    // ===== 邮箱验证码登录（无需密码） =====
    @PostMapping("/login-by-code")
    public Result loginByCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        if (email == null || email.trim().isEmpty()) {
            return Result.fail("邮箱不能为空");
        }
        if (code == null || code.trim().isEmpty()) {
            return Result.fail("验证码不能为空");
        }

        LoginInfo loginInfo;
        try {
            loginInfo = loginService.loginByCode(email.trim(), code.trim());
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }

        // 登录成功，生成 token
        String token = jwtUtil.generateToken(loginInfo.getId(), loginInfo.getRole());
        loginInfo.setToken(token);
        String refreshToken = jwtUtil.generateRefreshToken(loginInfo.getId(), loginInfo.getRole());
        loginInfo.setRefreshToken(refreshToken);

        return Result.success(loginInfo);
    }

    // ===== 通过邮箱重置密码 =====
    @PostMapping("/reset-password")
    public Result resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        String newPassword = body.get("newPassword");

        if (email == null || email.trim().isEmpty()) {
            return Result.fail("邮箱不能为空");
        }
        if (code == null || code.trim().isEmpty()) {
            return Result.fail("验证码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.fail("新密码不能为空");
        }

        String error = loginService.resetPassword(email.trim(), code.trim(), newPassword);
        if (error != null) {
            return Result.fail(error);
        }
        return Result.ok("密码重置成功，请使用新密码登录");
    }

    // ===== 通过邮箱找回用户名 =====
    @PostMapping("/find-username")
    public Result findUsername(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.trim().isEmpty()) {
            return Result.fail("邮箱不能为空");
        }

        String username = loginService.findUsernameByEmail(email.trim());
        if (username == null) {
            return Result.fail("该邮箱未绑定任何账号");
        }
        return Result.success(username);
    }
}
