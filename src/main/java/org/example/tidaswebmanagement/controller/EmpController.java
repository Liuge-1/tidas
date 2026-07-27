package org.example.tidaswebmanagement.controller;


import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tidaswebmanagement.anno.OperationLog;
import org.example.tidaswebmanagement.constant.BusinessConstants;
import org.example.tidaswebmanagement.exception.BusinessException;
import org.example.tidaswebmanagement.mapper.EmpMapper;
import org.example.tidaswebmanagement.pojo.*;
import org.example.tidaswebmanagement.service.EmpExcelService;
import org.example.tidaswebmanagement.service.EmpService;
import org.example.tidaswebmanagement.utils.UserContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
public class EmpController {

    private static final Logger logger= LoggerFactory.getLogger(EmpController.class);


    @Autowired
    private EmpService empService;
    @Autowired
    private EmpExcelService empExcelService;
    @Autowired
    private EmpMapper empMapper;



    //查询所有员工
    @GetMapping("/emps")
    public PageResult getEmps(@RequestParam Integer page, @RequestParam Integer pagesize){
        logger.info("getEmps");

        PageResult emps=empService.getEmps(page,pagesize);

        return emps;
    }

    //条件分页查询
    @GetMapping("/empsbyscan")
    public PageResult getEmpsbyscan(@RequestParam(required = false) String name, @RequestParam(required = false) String gender, @RequestParam(required = false) String startdate, @RequestParam(required = false) String enddate, @RequestParam Integer page, @RequestParam Integer pagesize){
        logger.info("getEmpsbyscan");

        PageResult empsbyscan = empService.getEmpsbyscan(name, gender, startdate, enddate, page, pagesize);
        return empsbyscan;
    }

    //新增员工
    @Transactional(rollbackFor = Exception.class) // 事务管理
    @OperationLog("新增员工")
    @PostMapping("/emps")
    public void addEmp(@RequestBody EMP_ULTIMATE emp_ultimate){
        Emp emp = emp_ultimate.getEmp();
        // 获取工作经历集合，不再强转单个对象
        List<EmpExpr> exprList = emp_ultimate.getEmpExpr();

        logger.info("新增员工基础参数:{}", emp);
        //保存员工基本信息
        empService.addEmp(emp);
        logger.info("员工基本信息保存成功，id:{}", emp.getId());

        // 遍历所有工作经历批量保存
        for (EmpExpr empExpr : exprList) {
            logger.info("待保存工作经历:{}", empExpr);
            empService.addEmpExpr(emp.getId(), empExpr.getBegin(), empExpr.getEnd(), empExpr.getCompany(), empExpr.getJob());
            logger.info("单条工作经历保存成功");
        }
        logger.info("全部员工数据新增完成");
    }

    //删除员工
    @Transactional(rollbackFor = Exception.class) // 事务管理
    @OperationLog("删除员工")
    @DeleteMapping("/emps")
    public Result deleteemp(@RequestParam Integer[] ids){
        logger.info("删除员工参数:{}", (Object) ids);
        empService.deleteemp(ids);
        logger.info("删除员工参数成功{}", (Object) ids);
        return Result.success();

    }

    //查询回显 (id)
    @GetMapping("/emps/{id}")
    public EMP_ULTIMATE getEmpById(@PathVariable Integer id){
        logger.info("查询回显参数:{}", id);
        EMP_ULTIMATE emp_ultimate = empService.getEmpById(id);
        logger.info("查询回显参数成功{}", emp_ultimate);
        return emp_ultimate;
    }


    //修改员工
    @OperationLog("修改员工")
    @PutMapping("/emps")

    public Result updateEmp(@RequestBody EMP_ULTIMATE emp_ultimate){
        // 非管理员只能修改自己的信息
        String role = UserContext.getRole();
        Integer currentUserId = UserContext.getUserId();
        Integer targetId = emp_ultimate.getEmp().getId();
        if (!BusinessConstants.ROLE_ADMIN.equals(role) && !currentUserId.equals(targetId)) {
            throw new BusinessException("只能修改自己的个人信息");
        }
        logger.info("修改员工参数:{}", emp_ultimate);
        empService.updateEmp(emp_ultimate);
        logger.info("修改员工参数成功{}", emp_ultimate);
        return Result.success();
    }

    // ==================== Excel 导入导出 ====================

    /** 下载空白 Excel 模板 */
    @GetMapping("/emps/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fn = URLEncoder.encode("员工导入模板.xlsx", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + fn);
        EasyExcel.write(response.getOutputStream(), EmpExcelDTO.class).sheet("员工信息").doWrite(List.of());
    }

    /** Excel 批量导入员工 */
    @OperationLog("批量导入员工")
    @PostMapping("/emps/import")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return Result.fail("请上传Excel文件");
        try {
            ImportResult result = empExcelService.importExcel(file);
            // 无论成功或失败，都把 ImportResult 放入 data 供前端展示详情
            String msg = result.hasErrors()
                    ? String.format("校验失败：共%d行，%d条错误", result.getTotalRows(), result.getFailRows())
                    : String.format("导入成功：共%d条", result.getSuccessRows());
            Result res = new Result();
            res.setCode(result.hasErrors() ? 0 : 1);
            res.setMsg(msg);
            res.setData(result);
            return res;
        } catch (IOException e) {
            logger.error("Excel导入异常", e);
            return Result.fail("文件解析失败：" + e.getMessage());
        }
    }

    /** Excel 导出（支持按部门/在职状态/入职日期筛选） */
    @GetMapping("/emps/export")
    public void exportExcel(@RequestParam(required = false) String deptName,
                            @RequestParam(required = false) String status,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate,
                            HttpServletResponse response) throws IOException {
        empExcelService.exportExcel(deptName, status, startDate, endDate, response);
    }

    /** 修改密码 */
    @OperationLog("修改密码")
    @PutMapping("/emps/password")
    public Result changePassword(@RequestBody Map<String, Object> body) {
        Integer userId = body.get("userId") != null ? Integer.valueOf(body.get("userId").toString()) : null;
        String oldPassword = (String) body.get("oldPassword");
        String newPassword = (String) body.get("newPassword");

        if (userId == null || oldPassword == null || newPassword == null) {
            throw new BusinessException("参数不完整");
        }
        if (newPassword.length() < 6) {
            throw new BusinessException("新密码长度不能少于6位");
        }
        int count = empMapper.checkPassword(userId, oldPassword);
        if (count == 0) {
            throw new BusinessException("原密码错误");
        }
        empMapper.updatePassword(userId, newPassword);
        logger.info("用户 {} 修改密码成功", userId);
        return Result.success();
    }

}
