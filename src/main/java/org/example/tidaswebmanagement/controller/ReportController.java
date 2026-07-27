package org.example.tidaswebmanagement.controller;

import org.example.tidaswebmanagement.pojo.*;
import org.example.tidaswebmanagement.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;


    //统计各职位人数
    @GetMapping("/getEmpJobData")
    public Result getEmpJobData(){
        Logger log = Logger.getLogger(ReportController.class.getName());
        log.info("统计员工职位数据");
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    //统计各性别人数

    @GetMapping("/getEmpGenderData")
    public Result getEmpGenderData(){
        Logger log = Logger.getLogger(ReportController.class.getName());
        log.info("统计员工性别数据");
        GenderOption genderOption = reportService.getEmpGenderData();

        return Result.success(genderOption);
    }

    //统计学员性别分布（复用GenderOption）
    @GetMapping("/getStudentGenderData")
    public Result getStudentGenderData(){
        Logger log = Logger.getLogger(ReportController.class.getName());
        log.info("统计学员性别数据");
        GenderOption genderOption = reportService.getStudentGenderData();
        return Result.success(genderOption);
    }

    //统计学员学历分布
    @GetMapping("/getStudentDegreeData")
    public Result getStudentDegreeData(){
        Logger log = Logger.getLogger(ReportController.class.getName());
        log.info("统计学员学历数据");
        StudentDegreeOption option = reportService.getStudentDegreeData();
        return Result.success(option);
    }

    //统计学员家乡地址分布
    @GetMapping("/getStudentAddressData")
    public Result getStudentAddressData(){
        Logger log = Logger.getLogger(ReportController.class.getName());
        log.info("统计学员家乡地址数据");
        StudentAddressOption option = reportService.getStudentAddressData();
        return Result.success(option);
    }

    //数据看板概览
    @GetMapping("/overview")
    public Result getOverview(){
        Logger log = Logger.getLogger(ReportController.class.getName());
        log.info("查询数据看板概览");
        java.util.Map<String, Object> overview = reportService.getOverview();
        return Result.success(overview);
    }
}
