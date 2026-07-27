package org.example.tidaswebmanagement.service;

import org.example.tidaswebmanagement.pojo.EMP_ULTIMATE;
import org.example.tidaswebmanagement.pojo.Emp;
import org.example.tidaswebmanagement.pojo.JobOption;
import org.example.tidaswebmanagement.pojo.PageResult;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EmpService {
    PageResult getEmps(Integer page, Integer pagesize);

    PageResult getEmpsbyscan(String name,String gender, String startdate, String enddate, Integer page, Integer pagesize);

    void addEmp(Emp emp);

    void addEmpExpr(Integer empId, LocalDate begin, LocalDate end, String company, String job);

    void deleteemp(Integer[] ids);

    EMP_ULTIMATE getEmpById(Integer id);

    void updateEmp(EMP_ULTIMATE empUltimate);



}
