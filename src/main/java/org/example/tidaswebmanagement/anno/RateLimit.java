package org.example.tidaswebmanagement.anno;

import java.lang.annotation.*;

/**
 * 接口限流注解 — 标记 Controller 方法，限制每分钟请求次数
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /** 每分钟允许的请求次数，默认60 */
    int value() default 60;
}
