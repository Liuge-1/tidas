package org.example.tidaswebmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** 注册请求 DTO */
public class EmpRegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[0-9a-zA-Z_.-]+@qq\\.com$", message = "仅支持QQ邮箱")
    private String email;

    @NotBlank(message = "验证码不能为空")
    private String code;

    private String name;
    private String gender;
    private String phone;
    private String job;

    // getter / setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
}
