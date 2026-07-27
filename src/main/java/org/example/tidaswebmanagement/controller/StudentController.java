package org.example.tidaswebmanagement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.tidaswebmanagement.anno.OperationLog;
import org.example.tidaswebmanagement.pojo.Result;
import org.example.tidaswebmanagement.pojo.Student;
import org.example.tidaswebmanagement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    //条件分页查询学员
    @GetMapping("/student")
    public Result getStudentPage(@RequestParam Integer page,
                                 @RequestParam Integer pagesize,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) Integer gender,
                                 @RequestParam(required = false) Integer clazzId) {
        logger.info("条件分页查询学员, page:{}, pagesize:{}, name:{}, gender:{}, clazzId:{}", page, pagesize, name, gender, clazzId);
        Page<Student> pageData = studentService.pageList(page, pagesize, name, gender, clazzId);
        return Result.success(pageData);
    }

    //根据id查询学员详情
    @GetMapping("/student/{id}")
    public Result getStudentById(@PathVariable Integer id) {
        logger.info("根据id查询学员详情, id:{}", id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    //新增学员
    @OperationLog("新增学员")
    @PostMapping("/student")
    public Result addStudent(@RequestBody Student student) {
        logger.info("新增学员信息:{}", student);
        studentService.addStudent(student);
        logger.info("新增学员成功");
        return Result.success();
    }

    //修改学员
    @OperationLog("修改学员")
    @PutMapping("/student")
    public Result updateStudent(@RequestBody Student student) {
        logger.info("修改学员信息:{}", student);
        studentService.updateStudent(student);
        logger.info("修改学员成功");
        return Result.success();
    }

    //删除学员
    @OperationLog("删除学员")
    @DeleteMapping("/student")
    public Result deleteStudent(@RequestParam Integer id) {
        logger.info("删除学员, id:{}", id);
        studentService.deleteStudent(id);
        logger.info("删除学员成功, id:{}", id);
        return Result.success();
    }
}