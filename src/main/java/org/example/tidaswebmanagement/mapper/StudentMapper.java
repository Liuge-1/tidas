package org.example.tidaswebmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.tidaswebmanagement.pojo.Student;
import java.util.List;
import java.util.Map;

public interface StudentMapper extends BaseMapper<Student> {

    //条件分页查询学员，联表班级，风格和Emp、Clazz保持一致
    List<Student> list(Page<Student> page,
                       @Param("name") String name,
                       @Param("gender") Integer gender,
                       @Param("clazzId") Integer clazzId);

    //根据id查询学员详情（联表带出班级名称）
    Student getStudentDetailById(Integer id);


    List<Map<String,Object>> countStudentGender();
    List<Map<String,Object>> countStudentDegree();
    List<Map<String,Object>> countStudentAddress();
}