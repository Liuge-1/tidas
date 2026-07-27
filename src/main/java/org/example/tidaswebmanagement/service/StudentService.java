package org.example.tidaswebmanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.tidaswebmanagement.pojo.Student;

public interface StudentService {

    //条件分页查询学员
    Page<Student> pageList(Integer pageNum, Integer pageSize, String name, Integer gender, Integer clazzId);

    //根据id查询学员详情
    Student getById(Integer id);

    //新增学员
    void addStudent(Student student);

    //修改学员
    void updateStudent(Student student);

    //删除学员
    void deleteStudent(Integer id);
}