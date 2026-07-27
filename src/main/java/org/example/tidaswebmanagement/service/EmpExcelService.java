package org.example.tidaswebmanagement.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tidaswebmanagement.mapper.DeptMapper;
import org.example.tidaswebmanagement.mapper.EmpMapper;
import org.example.tidaswebmanagement.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class EmpExcelService {

    private static final Logger log = LoggerFactory.getLogger(EmpExcelService.class);
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired private EmpMapper empMapper;
    @Autowired private DeptMapper deptMapper;

    // ==================== 导入 ====================

    /**
     * 全量校验 + 事务批量导入
     */
    @Transactional(rollbackFor = Exception.class)
    public ImportResult importExcel(MultipartFile file) throws IOException {
        // 1. 逐行读取
        List<EmpExcelDTO> rows = EasyExcel.read(file.getInputStream())
                .head(EmpExcelDTO.class).sheet().doReadSync();

        ImportResult result = new ImportResult();
        result.setTotalRows(rows.size());
        if (rows.isEmpty()) { result.addError(0, "Excel无数据"); return result; }

        // 2. 预加载部门和职位缓存
        Set<String> validDepts = new HashSet<>(deptMapper.findAllNames());
        Set<String> validJobs = new HashSet<>(deptMapper.findAllJobNames());
        Set<String> existingPhones = new HashSet<>(empMapper.getAllPhones());

        // 3. 全量校验
        List<Emp> validEmps = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            EmpExcelDTO dto = rows.get(i);
            int rowNum = i + 2; // Excel行号（跳过表头）
            List<String> rowErrors = validateRow(dto, validDepts, validJobs, existingPhones);
            if (!rowErrors.isEmpty()) {
                for (String err : rowErrors) result.addError(rowNum, err);
                continue;
            }
            Emp emp = toEmp(dto);
            validEmps.add(emp);
            existingPhones.add(dto.getPhone()); // 同一批内去重
        }

        // 4. 有错误 → 不插入，回滚事务
        if (result.hasErrors()) {
            result.setFailRows(result.getErrors().size());
            result.setSuccessRows(0);
            return result;
        }

        // 5. 全通过 → 批量插入
        for (Emp emp : validEmps) {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            emp.setRoleCode("emp");
            empMapper.insertSingle(emp);
        }
        result.setSuccessRows(validEmps.size());
        result.setFailRows(0);
        return result;
    }

    private List<String> validateRow(EmpExcelDTO dto, Set<String> depts,
                                      Set<String> jobs, Set<String> phones) {
        List<String> errs = new ArrayList<>();
        // 姓名必填
        if (dto.getName() == null || dto.getName().trim().isEmpty()) errs.add("姓名不能为空");
        // 手机号必填 + 格式 + 重复
        String phone = dto.getPhone();
        if (phone == null || phone.trim().isEmpty()) errs.add("手机号不能为空");
        else if (!PHONE_PATTERN.matcher(phone.trim()).matches()) errs.add("手机号格式错误");
        else if (phones.contains(phone.trim())) errs.add("手机号已存在");
        // 部门存在
        String dept = dto.getDeptName();
        if (dept != null && !dept.trim().isEmpty() && !depts.contains(dept.trim()))
            errs.add("部门【" + dept + "】不存在");
        // 岗位存在
        String job = dto.getJob();
        if (job != null && !job.trim().isEmpty() && !jobs.contains(job.trim()))
            errs.add("岗位【" + job + "】不存在");
        // 入职日期格式
        String date = dto.getEntryDate();
        if (date != null && !date.trim().isEmpty()) {
            try { LocalDate.parse(date.trim(), DATE_FMT); }
            catch (DateTimeParseException e) { errs.add("入职日期格式错误（应为yyyy-MM-dd）"); }
        }
        return errs;
    }

    private Emp toEmp(EmpExcelDTO dto) {
        Emp e = new Emp();
        e.setUsername("emp_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000));
        e.setPassword("123456");
        e.setName(dto.getName().trim());
        e.setPhone(dto.getPhone().trim());
        e.setJob(dto.getJob() != null ? dto.getJob().trim() : null);
        e.setStatus(dto.getStatus() != null ? dto.getStatus().trim() : "在职");
        e.setDeptName(dto.getDeptName() != null ? dto.getDeptName().trim() : null);
        if (dto.getEntryDate() != null && !dto.getEntryDate().trim().isEmpty())
            e.setEntryDate(LocalDate.parse(dto.getEntryDate().trim(), DATE_FMT));
        return e;
    }

    // ==================== 导出 ====================

    public void exportExcel(String deptName, String status, String startDate, String endDate,
                            HttpServletResponse response) throws IOException {
        // 分批查询（每次1000条）
        List<EmpExcelDTO> all = new ArrayList<>();
        int offset = 0, size = 1000;
        while (true) {
            List<Emp> batch = empMapper.listForExport(deptName, status, startDate, endDate, offset, size);
            if (batch.isEmpty()) break;
            for (Emp e : batch) {
                EmpExcelDTO dto = new EmpExcelDTO();
                dto.setName(e.getName());
                dto.setPhone(e.getPhone());
                dto.setDeptName(e.getDeptName());
                dto.setJob(e.getJob());
                dto.setEntryDate(e.getEntryDate() != null ? e.getEntryDate().format(DATE_FMT) : "");
                dto.setStatus(e.getStatus());
                all.add(dto);
            }
            offset += size;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("员工信息导出.xlsx", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + fileName);

        try (OutputStream out = response.getOutputStream()) {
            EasyExcel.write(out, EmpExcelDTO.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("员工信息")
                    .doWrite(all);
        }
    }
}
