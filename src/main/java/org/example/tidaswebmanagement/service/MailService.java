package org.example.tidaswebmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    // 内存存储验证码：key=email, value={code, expireTime}
    private final Map<String, CodeInfo> codeStore = new ConcurrentHashMap<>();

    private static class CodeInfo {
        String code;
        long expireTime; // 过期时间戳 (毫秒)
        CodeInfo(String code, long expireTime) { this.code = code; this.expireTime = expireTime; }
    }

    /**
     * 发送6位数字验证码到指定邮箱（仅支持 @qq.com）
     * @param email 邮箱地址
     * @param purpose 用途：register / login / reset-password
     * @return 错误信息，null 表示成功
     */
    public String sendCode(String email, String purpose) {
        if (email == null || !email.endsWith("@qq.com")) {
            return "目前仅支持QQ邮箱";
        }

        // 生成6位验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 根据用途生成不同的主题和内容
        String subject;
        String actionDesc;
        switch (purpose != null ? purpose : "register") {
            case "login":
                subject = "TIDAS 培训管理系统 - 登录验证码";
                actionDesc = "正在使用邮箱验证码登录 TIDAS 培训管理系统";
                break;
            case "reset-password":
                subject = "TIDAS 培训管理系统 - 密码重置验证码";
                actionDesc = "正在重置 TIDAS 培训管理系统的登录密码";
                break;
            default:
                subject = "TIDAS 培训管理系统 - 注册验证码";
                actionDesc = "正在注册 TIDAS 培训管理系统";
                break;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(email);
            message.setSubject(subject);
            message.setText("您好！\n\n"
                    + "您" + actionDesc + "，验证码如下：\n\n"
                    + "    " + code + "\n\n"
                    + "验证码 5 分钟内有效，请勿泄露给他人。\n"
                    + "如非本人操作，请忽略此邮件。\n\n"
                    + "TIDAS 培训管理系统");
            mailSender.send(message);

            // 5分钟过期
            codeStore.put(email, new CodeInfo(code, System.currentTimeMillis() + 5 * 60 * 1000));
            return null; // 成功
        } catch (Exception e) {
            return "验证码发送失败：" + e.getMessage();
        }
    }

    /**
     * 校验验证码
     * @return true=验证通过
     */
    public boolean verifyCode(String email, String code) {
        CodeInfo info = codeStore.get(email);
        if (info == null) return false;
        if (System.currentTimeMillis() > info.expireTime) {
            codeStore.remove(email);
            return false;
        }
        if (!info.code.equals(code)) return false;
        codeStore.remove(email); // 验证通过后立即删除，一次性使用
        return true;
    }
}
