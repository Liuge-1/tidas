package org.example.tidaswebmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** 离职提交 DTO */
public class ResignationSubmitDTO {

    @NotNull(message = "员工ID不能为空")
    private Integer empId;

    @NotBlank(message = "离职日期不能为空")
    private String resignationDate;

    private String reason;

    // getter / setter
    public Integer getEmpId() { return empId; }
    public void setEmpId(Integer empId) { this.empId = empId; }
    public String getResignationDate() { return resignationDate; }
    public void setResignationDate(String resignationDate) { this.resignationDate = resignationDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
