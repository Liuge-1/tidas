package org.example.tidaswebmanagement.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.tidaswebmanagement.constant.BusinessConstants;
import org.example.tidaswebmanagement.pojo.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器 —— 屏蔽后端报错堆栈，返回统一 Result
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    // 1. 自定义业务异常（优先级最高）
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.warn("业务异常 [{}]：{}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 2. 参数校验失败（@Valid 触发）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + "：" + f.getDefaultMessage())
                .collect(Collectors.joining("；"));
        log.warn("参数校验失败：{}", msg);
        return Result.fail(msg);
    }

    // 3. 数据库唯一约束冲突
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result handleDuplicateKeyException(SQLIntegrityConstraintViolationException e) {
        log.error("数据重复/唯一索引冲突", e);
        String msg = e.getMessage();
        if (msg != null && msg.contains("Duplicate entry")) {
            String val = msg.split(" ")[2];
            return Result.fail("数据【" + val + "】已存在，请勿重复添加");
        }
        return Result.fail("数据约束冲突，操作失败");
    }

    // 4. 数据库唯一约束冲突（Spring 包装后的异常，比 DataIntegrityViolationException 更精确）
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("唯一约束冲突", e);
        String msg = e.getMessage();
        if (msg != null && msg.contains("Duplicate entry")) {
            // 提取重复的值
            String[] parts = msg.split(" ");
            String val = parts.length > 2 ? parts[2] : "未知";
            return Result.fail("数据【" + val + "】已存在，请勿重复添加");
        }
        return Result.fail("数据重复或唯一约束冲突，请检查手机号/邮箱是否已被使用");
    }

    // 5. 数据完整性异常（外键、非空等）
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrityException(DataIntegrityViolationException e) {
        log.error("数据完整性异常", e);
        String msg = e.getMessage();
        if (msg != null && msg.contains("foreign key constraint fails")) {
            return Result.fail("该数据已被其他业务关联，无法删除");
        }
        if (msg != null && (msg.contains("cannot be null") || msg.contains("NOT NULL"))) {
            return Result.fail("存在必填字段未填写，请检查后重试");
        }
        return Result.fail("字段必填或数据格式不符合要求");
    }

    // 6. 文件大小超限
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleFileSizeException(MaxUploadSizeExceededException e) {
        log.error("上传文件过大", e);
        return Result.fail("上传文件超出最大限制");
    }

    // 7. 空指针
    @ExceptionHandler(NullPointerException.class)
    public Result handleNullPointException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.fail("数据为空，操作失败");
    }

    // 8. 非法参数
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgException(IllegalArgumentException e) {
        log.error("非法参数", e);
        return Result.fail("参数不合法：" + e.getMessage());
    }

    // 9. 兜底：未知异常
    @ExceptionHandler(Exception.class)
    public Result handleAllException(Exception e) {
        log.error("系统未知异常", e);
        return Result.fail(BusinessConstants.MSG_SERVER_ERROR);
    }
}
