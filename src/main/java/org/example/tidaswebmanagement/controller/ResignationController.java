package org.example.tidaswebmanagement.controller;

import jakarta.validation.Valid;
import org.example.tidaswebmanagement.anno.OperationLog;
import org.example.tidaswebmanagement.dto.ResignationSubmitDTO;
import org.example.tidaswebmanagement.exception.BusinessException;
import org.example.tidaswebmanagement.pojo.EmpResignation;
import org.example.tidaswebmanagement.pojo.PageResult;
import org.example.tidaswebmanagement.pojo.Result;
import org.example.tidaswebmanagement.service.EmpResignationService;
import org.example.tidaswebmanagement.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/resignation")
public class ResignationController {

    private static final Logger log = LoggerFactory.getLogger(ResignationController.class);

    @Autowired
    private EmpResignationService resignationService;

    /** 办理离职（仅管理员可调用，由 TokenInterceptor 保证） */
    @OperationLog("办理员工离职")
    @PostMapping("/submit")
    public Result resign(@Valid @RequestBody ResignationSubmitDTO dto) {
        Integer operatorId = UserContext.getUserId();
        try {
            LocalDate resignationDate = LocalDate.parse(dto.getResignationDate().trim());
            resignationService.resign(dto.getEmpId(), resignationDate,
                    dto.getReason() != null ? dto.getReason().trim() : "", operatorId);
            return Result.ok("离职办理成功");
        } catch (RuntimeException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /** 离职记录列表 */
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(new PageResult<>(resignationService.count(), resignationService.list(page, pageSize)));
    }
}
