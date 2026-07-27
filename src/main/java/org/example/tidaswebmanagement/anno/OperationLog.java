package org.example.tidaswebmanagement.anno;

import java.lang.annotation.*;

/**
 * 操作日志注解 — 标记 Controller 方法，自动记录操作审计日志
 * 只用于新增、修改、删除接口；查询接口不加此注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    /** 操作描述，如 "新增员工"、"删除部门" */
    String value();
}
