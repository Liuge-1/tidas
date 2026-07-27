package org.example.tidaswebmanagement.controller;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tidaswebmanagement.pojo.OperationLog;
import org.example.tidaswebmanagement.pojo.PageResult;
import org.example.tidaswebmanagement.pojo.Result;
import org.example.tidaswebmanagement.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志查询（仅管理员可访问，拦截器保护）
 * 日志只允许查询，不提供修改/删除接口
 */
@RestController
@RequestMapping("/operation-log")
public class OperationLogController {

    @Autowired
    private OperationLogService logService;

    /** 分页 + 筛选查询 */
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(required = false) Integer userId,
                       @RequestParam(required = false) String startTime,
                       @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String result,
                       @RequestParam(required = false) String operation) {
        List<OperationLog> rows = logService.listWithFilter(userId, startTime, endTime, result, operation, page, pageSize);
        long total = logService.countWithFilter(userId, startTime, endTime, result, operation);
        return Result.ok(new PageResult<>(total, rows));
    }

    /** 导出操作日志为 Excel */
    @GetMapping("/export")
    public void exportLogs(@RequestParam(required = false) Integer userId,
                           @RequestParam(required = false) String startTime,
                           @RequestParam(required = false) String endTime,
                           @RequestParam(required = false) String result,
                           @RequestParam(required = false) String operation,
                           HttpServletResponse response) throws IOException {
        List<OperationLog> logs = logService.listAllWithFilter(userId, startTime, endTime, result, operation);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 构建表头和数据
        List<List<String>> head = List.of(
                List.of("ID"), List.of("操作人"), List.of("操作描述"), List.of("请求方式"),
                List.of("接口地址"), List.of("IP"), List.of("参数"), List.of("结果"),
                List.of("错误信息"), List.of("时间")
        );

        List<List<Object>> data = logs.stream().map(log -> {
            List<Object> row = new java.util.ArrayList<>();
            row.add(log.getId());
            row.add(log.getUsername());
            row.add(log.getOperation());
            row.add(log.getMethod());
            row.add(log.getUrl());
            row.add(log.getIp());
            row.add(log.getParams());
            row.add(log.getResult());
            row.add(log.getErrorMsg());
            row.add(log.getCreateTime() != null ? log.getCreateTime().format(fmt) : "");
            return row;
        }).collect(Collectors.toList());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("操作日志导出.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("操作日志")
                .doWrite(data);
    }
}
