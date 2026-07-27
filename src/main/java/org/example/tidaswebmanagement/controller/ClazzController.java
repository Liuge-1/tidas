package org.example.tidaswebmanagement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.tidaswebmanagement.anno.OperationLog;
import org.example.tidaswebmanagement.pojo.Clazz;
import org.example.tidaswebmanagement.pojo.Result;
import org.example.tidaswebmanagement.service.ClazzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ClazzController {

    private static final Logger logger = LoggerFactory.getLogger(ClazzController.class);

    @Autowired
    private ClazzService clazzService;

    //条件分页查询班级
    @GetMapping("/clazz")
    public Result list(@RequestParam Integer page,
                       @RequestParam Integer pagesize,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Integer subject) {
        logger.info("条件分页查询班级, page:{}, pagesize:{}, name:{}, subject:{}", page, pagesize, name, subject);
        Page<Clazz> pageData = clazzService.pageList(page, pagesize, name, subject);
        return Result.success(pageData);
    }

    //查询全部班级（下拉选择）
    @GetMapping("/clazz/list")
    public Result listAll() {
        logger.info("查询全部班级下拉列表");
        List<Clazz> list = clazzService.listAll();
        return Result.success(list);
    }

    //根据id查询班级详情
    @GetMapping("/clazz/{id}")
    public Result getInfo(@PathVariable Integer id) {
        logger.info("根据id查询班级详情, id:{}", id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    //新增班级
    @OperationLog("新增班级")
    @PostMapping("/clazz")
    public Result add(@RequestBody Clazz clazz) {
        logger.info("新增班级信息:{}", clazz);
        clazzService.add(clazz);
        logger.info("新增班级成功");
        return Result.success();
    }

    //修改班级
    @OperationLog("修改班级")
    @PutMapping("/clazz")
    public Result update(@RequestBody Clazz clazz) {
        logger.info("修改班级信息:{}", clazz);
        clazzService.update(clazz);
        logger.info("修改班级成功");
        return Result.success();
    }

    //删除班级
    @OperationLog("删除班级")
    @DeleteMapping("/clazz")
    public Result delete(@RequestParam Integer id) {
        logger.info("删除班级, id:{}", id);
        try {
            clazzService.delete(id);
            logger.info("删除班级成功, id:{}", id);
            return Result.success();
        } catch (RuntimeException e) {
            logger.error("删除班级失败：{}", e.getMessage());
            return Result.fail(e.getMessage());
        }
    }
}