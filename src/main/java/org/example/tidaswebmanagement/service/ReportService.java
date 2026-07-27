package org.example.tidaswebmanagement.service;

import org.example.tidaswebmanagement.pojo.GenderOption;
import org.example.tidaswebmanagement.pojo.JobOption;
import org.example.tidaswebmanagement.pojo.StudentAddressOption;
import org.example.tidaswebmanagement.pojo.StudentDegreeOption;

import java.util.Map;

public interface ReportService {
    JobOption getEmpJobData();

    GenderOption getEmpGenderData();


    //学员性别统计
    GenderOption getStudentGenderData();
    //学员学历统计
    StudentDegreeOption getStudentDegreeData();
    //学员家乡统计
    StudentAddressOption getStudentAddressData();

    //数据看板概览
    Map<String, Object> getOverview();

}
