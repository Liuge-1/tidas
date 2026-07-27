package org.example.tidaswebmanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.tidaswebmanagement.pojo.Clazz;
import java.util.List;

public interface ClazzService {
    //条件分页查询
    Page<Clazz> pageList(Integer pageNum, Integer pageSize, String name, Integer subject);
    //查询所有班级（下拉）
    List<Clazz> listAll();
    //根据id查询
    Clazz getById(Integer id);
    //新增
    void add(Clazz clazz);
    //修改
    void update(Clazz clazz);
    //删除班级（带业务校验：存在学员不能删）
    void delete(Integer id);
    //校验班级id是否存在（供学员新增调用）
    boolean existsById(Integer clazzId);


}