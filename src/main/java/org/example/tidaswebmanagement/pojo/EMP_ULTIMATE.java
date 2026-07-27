package org.example.tidaswebmanagement.pojo;

import org.example.tidaswebmanagement.pojo.Emp;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class EMP_ULTIMATE {
    private Emp emp;
    private List<EmpExpr> empExpr;

    public Emp getEmp() {
        return emp;
    }

    public void setEmp(Emp emp) {
        this.emp = emp;
    }

    public List<EmpExpr> getEmpExpr() {
        return empExpr;
    }

    public void setEmpExpr(List<EmpExpr> empExpr) {
        this.empExpr = empExpr;
    }
}