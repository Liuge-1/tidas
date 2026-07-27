package org.example.tidaswebmanagement.service;

import org.example.tidaswebmanagement.mapper.OperationLogMapper;
import org.example.tidaswebmanagement.pojo.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogService {

    @Autowired
    private OperationLogMapper mapper;

    public void save(OperationLog log) {
        mapper.insert(log);
    }

    public List<OperationLog> listWithFilter(Integer userId, String startTime,
                                              String endTime, String result,
                                              String operation,
                                              int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return mapper.listWithFilter(userId, startTime, endTime, result, operation, offset, pageSize);
    }

    public long countWithFilter(Integer userId, String startTime, String endTime,
                                String result, String operation) {
        return mapper.countWithFilter(userId, startTime, endTime, result, operation);
    }

    /** 导出：不分页，返回全部符合筛选条件的记录 */
    public List<OperationLog> listAllWithFilter(Integer userId, String startTime,
                                                 String endTime, String result,
                                                 String operation) {
        return mapper.listAllWithFilter(userId, startTime, endTime, result, operation);
    }
}
