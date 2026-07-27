package org.example.tidaswebmanagement.mapper;

import org.apache.ibatis.annotations.*;
import org.example.tidaswebmanagement.pojo.Dept;
import org.example.tidaswebmanagement.pojo.Emp;

import java.util.List;

@Mapper
public interface DeptMapper {

    //查询所有部门

    //方式一
//    @Results({
//            @Result(column = "id", property = "id"),
//            @Result(column = "name", property = "name"),
//            @Result(column = "create_time", property = "createTime"),
//            @Result(column = "update_time", property = "updateTime")
//    })

    //方式二
    @Select("select id, name, create_time, update_time from dept order by id asc")
    List<Dept> findAll();

    @Delete("delete from dept where id=#{id}")
    void delete(@Param("id") Integer id);

    @Insert("insert into dept(id,name,create_time,update_time) values(#{id},#{name},now(),now())")
    void add(Dept dept);

    @Select("select * from dept where id=#{id}")
    List<Dept> findid(int id);

    @Update("update dept set name=#{name},update_time=#{updateTime} where id=#{id}")
    void update(Dept dept);
    @Select("SELECT COUNT(*) FROM emp WHERE dept_id = #{deptId}")
    int countEmpByDeptId(int deptId);

    @Select("SELECT * FROM emp WHERE dept_id = #{deptId}")
    List<Emp> getEmpsByDeptId(Integer deptId);

    @Update("UPDATE emp SET dept_id = #{deptId} WHERE id = #{empId}")
    void updateEmpDept(@Param("empId") Integer empId, @Param("deptId") Integer deptId);

    // Excel 导入校验用：所有部门名
    @Select("SELECT name FROM dept")
    List<String> findAllNames();

    // Excel 导入校验用：所有已有岗位
    @Select("SELECT DISTINCT job FROM emp WHERE job IS NOT NULL AND job != ''")
    List<String> findAllJobNames();

    // 部门总数
    @Select("SELECT COUNT(*) FROM dept")
    long countAll();
}
