package org.example.tidaswebmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.tidaswebmanagement.pojo.Notice;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("SELECT * FROM notice ORDER BY is_top DESC, create_time DESC")
    List<Notice> findByPage(Page<Notice> page);

    @Select("SELECT * FROM notice ORDER BY is_top DESC, create_time DESC LIMIT 3")
    List<Notice> findTop3();
}
