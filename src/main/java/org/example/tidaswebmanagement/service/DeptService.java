package org.example.tidaswebmanagement.service;

import org.example.tidaswebmanagement.pojo.Dept;
import org.example.tidaswebmanagement.pojo.Emp;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();
     void delete(int id);
     void add(Dept dept);


    List<Dept> findid(Integer id);


    void update(Dept dept);
    List<Emp> getEmpsByDeptId(Integer deptId);

    void addEmpToDept(Integer deptId, Integer empId);
    void removeEmpFromDept(Integer deptId, Integer empId);
}
