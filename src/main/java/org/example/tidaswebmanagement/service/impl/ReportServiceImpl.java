package org.example.tidaswebmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.tidaswebmanagement.mapper.ClazzMapper;
import org.example.tidaswebmanagement.mapper.DeptMapper;
import org.example.tidaswebmanagement.mapper.EmpMapper;
import org.example.tidaswebmanagement.mapper.EmpResignationMapper;
import org.example.tidaswebmanagement.mapper.StudentMapper;
import org.example.tidaswebmanagement.pojo.GenderOption;
import org.example.tidaswebmanagement.pojo.JobOption;
import org.example.tidaswebmanagement.pojo.StudentAddressOption;
import org.example.tidaswebmanagement.pojo.StudentDegreeOption;
import org.example.tidaswebmanagement.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private EmpResignationMapper empResignationMapper;

    @Override
    public JobOption getEmpJobData() {

        //获取数据
        List<Map<String, Object>> jobOptions = empMapper.getJobOptions();

        //封装数据
        List<String> jobs = jobOptions.stream().map(option -> (String) option.get("pos")).toList();
        List<Long> sums = jobOptions.stream().map(option -> (Long) option.get("num")).toList();
        return new JobOption(jobs, sums);

    }

    @Override
    public GenderOption getEmpGenderData() {
        //获取数据
        List<Map<String, Object>> genderOptions = empMapper.getGenderOptions();

        //封装
        List<String> genders = genderOptions.stream().map(option -> (String) option.get("gender")).toList();
        List<Long> sums = genderOptions.stream().map(option -> (Long) option.get("num")).toList();
        return new GenderOption(genders, sums);
    }

    @Override
    public GenderOption getStudentGenderData() {
        List<Map<String, Object>> list = studentMapper.countStudentGender();
        List<String> genderList = new ArrayList<>();
        List<Long> sumList = new ArrayList<>();

        for (Map<String, Object> map : list) {
            Integer gender = Integer.valueOf(map.get("gender").toString());
            Long cnt = Long.valueOf(map.get("cnt").toString());
            genderList.add(gender == 1 ? "男" : "女");
            sumList.add(cnt);
        }
        return new GenderOption(genderList, sumList);
    }

    @Override
    public StudentDegreeOption getStudentDegreeData() {
        Map<Integer, String> degreeMap = new HashMap<>();
        degreeMap.put(1,"初中");
        degreeMap.put(2,"高中");
        degreeMap.put(3,"大专");
        degreeMap.put(4,"本科");
        degreeMap.put(5,"硕士");
        degreeMap.put(6,"博士");

        List<Map<String, Object>> list = studentMapper.countStudentDegree();
        List<String> degreeList = new ArrayList<>();
        List<Long> sumList = new ArrayList<>();

        for (Map<String, Object> map : list) {
            Integer degree = Integer.valueOf(map.get("degree").toString());
            Long cnt = Long.valueOf(map.get("cnt").toString());
            degreeList.add(degreeMap.get(degree));
            sumList.add(cnt);
        }
        return new StudentDegreeOption(degreeList, sumList);
    }

    @Override
    public StudentAddressOption getStudentAddressData() {
        List<Map<String, Object>> list = studentMapper.countStudentAddress();
        List<String> addressList = new ArrayList<>();
        List<Long> sumList = new ArrayList<>();

        for (Map<String, Object> map : list) {
            String address = map.get("address").toString();
            Long cnt = Long.valueOf(map.get("cnt").toString());
            addressList.add(address);
            sumList.add(cnt);
        }
        return new StudentAddressOption(addressList, sumList);
    }

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> overview = new HashMap<>();

        long totalEmps = empMapper.countTotal();
        long activeEmps = empMapper.countByStatus("在职");
        long resignedEmps = empMapper.countByStatus("离职");
        long totalDepts = deptMapper.countAll();
        long totalClazz = clazzMapper.selectCount(null);
        long totalStudents = studentMapper.selectCount(null);
        long newHiresThisMonth = empMapper.countNewHiresThisMonth();
        long resignationsThisMonth = empResignationMapper.countResignationsThisMonth();

        overview.put("totalEmps", totalEmps);
        overview.put("activeEmps", activeEmps);
        overview.put("resignedEmps", resignedEmps);
        overview.put("totalDepts", totalDepts);
        overview.put("totalClazz", totalClazz);
        overview.put("totalStudents", totalStudents);
        overview.put("newHiresThisMonth", newHiresThisMonth);
        overview.put("resignationsThisMonth", resignationsThisMonth);

        return overview;
    }
}
