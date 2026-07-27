package org.example.tidaswebmanagement.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * Excel 导入/导出 DTO——与 EasyExcel 表头映射
 */
public class EmpExcelDTO {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("部门")
    private String deptName;

    @ExcelProperty("岗位")
    private String job;

    @ExcelProperty("入职日期")
    private String entryDate;

    @ExcelProperty("在职状态")
    private String status;

    // getter / setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
    public String getEntryDate() { return entryDate; }
    public void setEntryDate(String entryDate) { this.entryDate = entryDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
