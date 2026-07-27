package org.example.tidaswebmanagement.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.tidaswebmanagement.mapper.ClazzMapper;
import org.example.tidaswebmanagement.pojo.Clazz;
import org.example.tidaswebmanagement.service.ClazzService;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Resource
    private ClazzMapper clazzMapper;

    @Override
    public Page<Clazz> pageList(Integer pageNum, Integer pageSize, String name, Integer subject) {
        Page<Clazz> page = new Page<>(pageNum, pageSize);
        //传入page，自动物理分页
        List<Clazz> records = clazzMapper.list(page, name, subject);
        page.setRecords(records);
        return page;
    }

    @Override
    public List<Clazz> listAll() {
        return clazzMapper.listAll();
    }



    @Override
    public void add(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.updateById(clazz);
    }

    @Override
    public void delete(Integer id) {
        //业务校验：班级下是否存在学员
        Long count = clazzMapper.countStudentByClazzId(id);
        if (count > 0) {
            throw new RuntimeException("该班级下存在学员，禁止删除！");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public boolean existsById(Integer clazzId) {
        return getById(clazzId) != null;
    }
    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getClazzDetailById(id);
    }
}