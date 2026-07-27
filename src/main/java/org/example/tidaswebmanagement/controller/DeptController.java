package org.example.tidaswebmanagement.controller;

import org.example.tidaswebmanagement.anno.OperationLog;
import org.example.tidaswebmanagement.pojo.Dept;
import org.example.tidaswebmanagement.pojo.Emp;
import org.example.tidaswebmanagement.pojo.Result;
import org.example.tidaswebmanagement.service.DeptService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DeptController.class);

    @Autowired
    private DeptService deptService;

    @RequestMapping(value = "/depts", method = RequestMethod.GET)
    public Result get() {
        logger.info("查询全部部门数据");
        List<Dept> deptlist = deptService.findAll();
        return Result.success(deptlist);
    }

    @OperationLog("删除部门")
    @DeleteMapping("/depts")
    public Result delete(@RequestParam(value = "id", required = false) Integer deleid) {
        logger.info("删除id为" + deleid + "的部门数据");
        try {
            deptService.delete(deleid);
            return Result.success();
        } catch (RuntimeException e) {
            logger.error("删除部门失败：{}", e.getMessage());
            return Result.fail(e.getMessage());
        }
    }

    @OperationLog("新增部门")
    @PostMapping("/depts")
    public Result add(@RequestParam Integer id, @RequestParam String name) {
        Dept dept = new Dept();
        dept.setId(id);
        dept.setName(name);
        deptService.add(dept);
        return Result.success();
    }

    @GetMapping("/depts/{id}")
    public Result idselect(@PathVariable Integer id) {
        logger.info("查询id为" + id + "的部门");
        List<Dept> iddeptlist = deptService.findid(id);
        return Result.success(iddeptlist);
    }

    @OperationLog("修改部门")
    @PutMapping("/depts")
    public Result update(@RequestBody Dept dept) {
        logger.info("修改部门：" + dept);
        deptService.update(dept);
        return Result.success();
    }

    // 查询部门下的员工
    @GetMapping("/depts/{id}/employees")
    public Result getEmpsByDept(@PathVariable Integer id) {
        logger.info("查询部门id为{}的员工", id);
        List<Emp> list = deptService.getEmpsByDeptId(id);
        return Result.success(list);
    }

    // 给部门添加员工
    @OperationLog("添加员工到部门")
    @PutMapping("/depts/{deptId}/emps/{empId}")
    public Result addEmpToDept(@PathVariable Integer deptId, @PathVariable Integer empId) {
        deptService.addEmpToDept(deptId, empId);
        return Result.success();
    }

    // 从部门移除员工
    @OperationLog("从部门移除员工")
    @DeleteMapping("/depts/{deptId}/emps/{empId}")
    public Result removeEmpFromDept(@PathVariable Integer deptId, @PathVariable Integer empId) {
        deptService.removeEmpFromDept(deptId, empId);
        return Result.success();
    }
}