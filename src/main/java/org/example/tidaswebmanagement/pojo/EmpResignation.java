package org.example.tidaswebmanagement.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("emp_resignation")
public class EmpResignation {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("emp_id")
    private Integer empId;
    @TableField("emp_name")
    private String empName;
    @TableField("emp_phone")
    private String empPhone;
    @TableField("emp_job")
    private String empJob;
    @TableField("dept_name")
    private String deptName;
    @TableField("entry_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDate;
    @TableField("resignation_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignationDate;
    private String reason;
    @TableField("operator_id")
    private Integer operatorId;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // getter / setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getEmpId() { return empId; }
    public void setEmpId(Integer empId) { this.empId = empId; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public String getEmpPhone() { return empPhone; }
    public void setEmpPhone(String empPhone) { this.empPhone = empPhone; }
    public String getEmpJob() { return empJob; }
    public void setEmpJob(String empJob) { this.empJob = empJob; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    public LocalDate getResignationDate() { return resignationDate; }
    public void setResignationDate(LocalDate resignationDate) { this.resignationDate = resignationDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getOperatorId() { return operatorId; }
    public void setOperatorId(Integer operatorId) { this.operatorId = operatorId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
