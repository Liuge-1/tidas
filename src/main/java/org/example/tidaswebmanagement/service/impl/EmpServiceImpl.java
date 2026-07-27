package org.example.tidaswebmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.tidaswebmanagement.exception.BusinessException;
import org.example.tidaswebmanagement.mapper.EmpExprMapper;
import org.example.tidaswebmanagement.mapper.EmpMapper;
import org.example.tidaswebmanagement.pojo.*;
import org.example.tidaswebmanagement.service.EmpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    private static final Logger log = LoggerFactory.getLogger(EmpServiceImpl.class);

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    // 使用 MyBatis-Plus 分页插件
    @Override
    public PageResult getEmps(Integer pageNum, Integer pageSize) {
        Page<Emp> page = new Page<>(pageNum, pageSize);
        List<Emp> emps = empMapper.getEmps(page);
        return new PageResult(page.getTotal(), emps);
    }

    @Override
    public PageResult getEmpsbyscan(String name, String gender, String startdate,
                                    String enddate, Integer pageNum, Integer pageSize) {
        Page<Emp> page = new Page<>(pageNum, pageSize);
        List<Emp> emps = empMapper.getEmpsbyscan(name, gender, startdate, enddate, page);
        return new PageResult(page.getTotal(), emps);
    }

    @Override
    public void addEmp(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.addEmp(emp);
    }

    @Override
    public void addEmpExpr(Integer empId, LocalDate begin, LocalDate end, String company, String job) {
        // 改用独立经历Mapper，封装实体传入
        EmpExpr empExpr = new EmpExpr();
        empExpr.setEmpId(empId);
        empExpr.setBegin(begin);
        empExpr.setEnd(end);
        empExpr.setCompany(company);
        empExpr.setJob(job);
        empExprMapper.addEmpExpr(empExpr);
    }

    @Override
    public void deleteemp(Integer[] ids) {
        List<Integer> idList = List.of(ids);
        // 删除员工主表数据
        empMapper.deleteEmps(idList);
        // 循环删除每一个员工对应的全部工作经历
        for (Integer empId : idList) {
            empExprMapper.deleteEmpExpr(empId);
        }
    }

    @Override
    public EMP_ULTIMATE getEmpById(Integer id) {
        EMP_ULTIMATE vo = new EMP_ULTIMATE();
        // 员工基础信息
        Emp emp = empMapper.getEmpBaseById(id);
        vo.setEmp(emp);
        // 查询该员工工作经历，切换EmpExprMapper
        List<EmpExpr> exprList = empExprMapper.selectEmpExpr(id);
        vo.setEmpExpr(exprList);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmp(EMP_ULTIMATE vo) {
        Emp emp = vo.getEmp();
        List<EmpExpr> exprList = vo.getEmpExpr();
        Integer empId = emp.getId();

        // 更新员工基础信息（XML动态SQL只更新非空字段），带乐观锁版本校验
        emp.setUpdateTime(LocalDateTime.now());
        log.info("updateEmp - 开始更新员工 id={}, name={}, gender={}, phone={}, email={}, job={}, version={}",
                emp.getId(), emp.getName(), emp.getGender(), emp.getPhone(), emp.getEmail(), emp.getJob(), emp.getVersion());
        int rows = empMapper.updateEmp(emp);
        log.info("updateEmp - SQL执行结果 rows={}", rows);
        if (rows == 0) {
            throw new BusinessException("数据已被他人修改，请刷新页面后重试");
        }

        // 仅当前端显式传入了工作经历列表时才更新（null 表示不修改工作经历）
        if (exprList != null) {
            log.info("updateEmp - 开始处理工作经历 exprList.size={}", exprList.size());
            // 1. 删除当前员工全部旧工作经历
            empExprMapper.deleteEmpExpr(empId);

            // 2. 循环新增前端传来的新工作经历
            if (!exprList.isEmpty()) {
                for (EmpExpr empExpr : exprList) {
                    empExpr.setEmpId(empId);
                    empExprMapper.addEmpExpr(empExpr);
                }
            }
        } else {
            log.info("updateEmp - 未传入工作经历，跳过处理");
        }
    }


}