package org.example.tidaswebmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.tidaswebmanagement.pojo.OperationLog;

import java.util.List;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /** 动态筛选 + 分页 */
    List<OperationLog> listWithFilter(@Param("userId") Integer userId,
                                      @Param("startTime") String startTime,
                                      @Param("endTime") String endTime,
                                      @Param("result") String result,
                                      @Param("operation") String operation,
                                      @Param("offset") int offset,
                                      @Param("size") int size);

    /** 筛选条件下的总数 */
    long countWithFilter(@Param("userId") Integer userId,
                         @Param("startTime") String startTime,
                         @Param("endTime") String endTime,
                         @Param("result") String result,
                         @Param("operation") String operation);

    /** 动态筛选（不分页，用于导出） */
    List<OperationLog> listAllWithFilter(@Param("userId") Integer userId,
                                         @Param("startTime") String startTime,
                                         @Param("endTime") String endTime,
                                         @Param("result") String result,
                                         @Param("operation") String operation);
}
