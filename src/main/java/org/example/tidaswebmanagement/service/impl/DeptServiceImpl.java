package org.example.tidaswebmanagement.service.impl;

import org.example.tidaswebmanagement.mapper.DeptMapper;
import org.example.tidaswebmanagement.pojo.Dept;
import org.example.tidaswebmanagement.pojo.Emp;
import org.example.tidaswebmanagement.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    @Override
    public void delete(int id) {
        int count = deptMapper.countEmpByDeptId(id);
        if (count > 0) {
            throw new RuntimeException("该部门下存在员工，无法删除！");
        }
        deptMapper.delete(id);
    }

    @Override
    public void add(Dept dept) {
        deptMapper.add(dept);
    }

    @Override
    public List<Dept> findid(Integer id) {
        return deptMapper.findid(id);
    }

    @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }

    @Override
    public List<Emp> getEmpsByDeptId(Integer deptId) {
        return deptMapper.getEmpsByDeptId(deptId);
    }
    @Override
    public void addEmpToDept(Integer deptId, Integer empId) {
        deptMapper.updateEmpDept(empId, deptId);
    }

    @Override
    public void removeEmpFromDept(Integer deptId, Integer empId) {
        deptMapper.updateEmpDept(empId, null);
    }
}