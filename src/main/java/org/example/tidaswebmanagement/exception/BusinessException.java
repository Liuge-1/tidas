package org.example.tidaswebmanagement.exception;

/** 自定义业务异常，抛出后由 GlobalExceptionHandler 统一处理 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 0;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() { return code; }
}
