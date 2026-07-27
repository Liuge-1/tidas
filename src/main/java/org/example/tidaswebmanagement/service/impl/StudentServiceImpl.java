package org.example.tidaswebmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.tidaswebmanagement.mapper.StudentMapper;
import org.example.tidaswebmanagement.pojo.Student;
import org.example.tidaswebmanagement.service.StudentService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Override
    public Page<Student> pageList(Integer pageNum, Integer pageSize, String name, Integer gender, Integer clazzId) {
        Page<Student> page = new Page<>(pageNum, pageSize);
        List<Student> records = studentMapper.list(page, name, gender, clazzId);
        page.setRecords(records);
        return page;
    }

    @Override
    public Student getById(Integer id) {
        return studentMapper.getStudentDetailById(id);
    }

    @Override
    public void addStudent(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Override
    public void updateStudent(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.updateById(student);
    }

    @Override
    public void deleteStudent(Integer id) {
        studentMapper.deleteById(id);
    }
}