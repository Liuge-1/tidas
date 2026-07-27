package org.example.tidaswebmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.tidaswebmanagement.pojo.Clazz;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ClazzMapper extends BaseMapper<Clazz> {
    //条件分页查询班级(左连接员工，带出班主任姓名)
    //条件分页查询班级(左连接员工，带出班主任姓名)
    List<Clazz> list(@Param("page") Page<Clazz> page,
                     @Param("name") String name,
                     @Param("subject") Integer subject);

    // 查询全部班级（下拉框使用，只返回id、name）
    List<Clazz> listAll();

    // 根据班级id统计学员数量（删除前业务校验）
    Long countStudentByClazzId(Integer clazzId);

    Clazz getClazzDetailById(Integer id);
}