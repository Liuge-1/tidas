package org.example.tidaswebmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.tidaswebmanagement.pojo.EmpResignation;

import java.util.List;

@Mapper
public interface EmpResignationMapper extends BaseMapper<EmpResignation> {

    @Insert("INSERT INTO emp_resignation (emp_id, emp_name, emp_phone, emp_job, dept_name, entry_date, resignation_date, reason, operator_id, create_time) " +
            "VALUES (#{empId}, #{empName}, #{empPhone}, #{empJob}, #{deptName}, #{entryDate}, #{resignationDate}, #{reason}, #{operatorId}, NOW())")
    void insertResignation(EmpResignation r);

    @Select("SELECT * FROM emp_resignation ORDER BY create_time DESC LIMIT #{offset}, #{size}")
    List<EmpResignation> listByPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM emp_resignation")
    long count();

    /** 本月离职人数 */
    @Select("SELECT COUNT(*) FROM emp_resignation WHERE resignation_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01') AND resignation_date <= LAST_DAY(CURDATE())")
    long countResignationsThisMonth();
}
