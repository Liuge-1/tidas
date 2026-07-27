package org.example.tidaswebmanagement.mapper;

import org.apache.ibatis.annotations.*;
import org.example.tidaswebmanagement.pojo.EmpExpr;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    //1.新增工作经历：直接传入实体，代码简洁（保留注解，无动态逻辑无需XML）
    @Insert("INSERT INTO emp_expr (emp_id, begin, end, company, job) VALUES (#{empId}, #{begin}, #{end}, #{company}, #{job})")
    void addEmpExpr(EmpExpr empExpr);

    //2.删除某员工所有工作经历（保留注解）
    @Delete("DELETE FROM emp_expr WHERE emp_id = #{empId}")
    void deleteEmpExpr(@Param("empId") Integer empId);

    //3.单条修改工作经历：删掉@Update注解，由EmpExprMapper.xml实现动态更新
    void updateEmpExpr(EmpExpr empExpr);

    //4.查询某员工所有工作经历（保留注解）
    @Select("SELECT * FROM emp_expr WHERE emp_id = #{empId}")
    List<EmpExpr> selectEmpExpr(@Param("empId") Integer empId);
}