package org.example.tidaswebmanagement.service;

import org.example.tidaswebmanagement.exception.BusinessException;
import org.example.tidaswebmanagement.mapper.EmpMapper;
import org.example.tidaswebmanagement.mapper.EmpResignationMapper;
import org.example.tidaswebmanagement.pojo.Emp;
import org.example.tidaswebmanagement.pojo.EmpResignation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmpResignationService {

    private static final Logger log = LoggerFactory.getLogger(EmpResignationService.class);

    @Autowired private EmpResignationMapper resignationMapper;
    @Autowired private EmpMapper empMapper;

    /**
     * 办理离职 —— 两次 DB 操作，同一事务
     * ① 更新 emp.status = '离职'
     * ② 新增 emp_resignation 记录
     * 任意一步失败 → 全部回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public void resign(Integer empId, LocalDate resignationDate, String reason, Integer operatorId) {
        // 查员工当前信息
        Emp emp = empMapper.getEmpBaseById(empId);
        if (emp == null) throw new BusinessException("员工不存在");
        if ("离职".equals(emp.getStatus())) throw new BusinessException("该员工已是离职状态，不可重复离职");

        // ① 更新在职状态
        empMapper.updateStatus(empId, "离职");

        // ② 插入离职记录
        EmpResignation r = new EmpResignation();
        r.setEmpId(emp.getId());
        r.setEmpName(emp.getName());
        r.setEmpPhone(emp.getPhone());
        r.setEmpJob(emp.getJob());
        r.setDeptName(emp.getDeptName());
        r.setEntryDate(emp.getEntryDate());
        r.setResignationDate(resignationDate);
        r.setReason(reason);
        r.setOperatorId(operatorId);
        resignationMapper.insertResignation(r);

        log.info("员工 {} (id={}) 离职办理完成", emp.getName(), empId);
    }

    /** 离职列表 */
    public List<EmpResignation> list(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return resignationMapper.listByPage(offset, pageSize);
    }

    public long count() {
        return resignationMapper.count();
    }
}
