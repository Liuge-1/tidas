package org.example.tidaswebmanagement.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.example.tidaswebmanagement.pojo.Emp;
import org.example.tidaswebmanagement.pojo.JobOption;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

    // 查询全部员工（关联部门名）- 支持分页
    @Select("select e.*,d.name as deptName from emp e left join dept d on e.dept_id=d.id")
    List<Emp> getEmps(Page<Emp> page);

    // 条件分页查询（xml方式）
    List<Emp> getEmpsbyscan(@Param("name") String name,
                            @Param("gender") String gender,
                            @Param("startDate") String startDate,
                            @Param("endDate") String endDate,
                            Page<Emp> page);

    // 新增员工（主键回填 + 时间字段）
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time, version) " +
            "values(#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime}, 0)")
    void addEmp(Emp emp);

    // 批量删除员工主表数据
    @Delete("<script>" +
            "DELETE FROM emp WHERE id IN " +
            "<foreach collection='ids' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    void deleteEmps(@Param("ids") List<Integer> ids);

    // 根据id查询单条员工+部门信息
    @Select("SELECT e.*,d.name AS deptName FROM emp e LEFT JOIN dept d ON e.dept_id = d.id WHERE e.id = #{id}")
    Emp getEmpBaseById(@Param("id") Integer id);

    // 动态更新员工基础信息（xml实现，已删除@Update注解），返回受影响行数
    int updateEmp(Emp emp);


    //统计工作人数
    @Select("select job as pos, count(*) as num from emp group by job order by num desc")
    List<Map<String, Object>> getJobOptions();

    //统计性别
    @Select("select " +
            "(case gender " +
            "when '男' then '男' " +
            "when '女' then '女' " +
            "else '未知' end) gender, " +
            "count(*) num " +
            "from emp " +
            "group by gender " +
            "order by num desc")
    List<Map<String, Object>> getGenderOptions();

    // ===== Excel 导入导出辅助 =====

    /** 获取所有已存在的手机号（用于导入去重校验） */
    @Select("SELECT phone FROM emp WHERE phone IS NOT NULL AND phone != ''")
    List<String> getAllPhones();

    /** 单条插入员工（含全部字段，用于批量导入） */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO emp (username, password, name, gender, phone, email, role_code, job, status, entry_date, dept_id, create_time, update_time, version) " +
            "VALUES (#{username}, #{password}, #{name}, #{gender}, #{phone}, #{email}, 'emp', #{job}, #{status}, #{entryDate}, #{deptId}, NOW(), NOW(), 0)")
    void insertSingle(Emp emp);

    /** 更新员工在职状态 */
    @Update("UPDATE emp SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") String status);

    /** 导出：按条件分批查询 */
    List<Emp> listForExport(@Param("deptName") String deptName,
                            @Param("status") String status,
                            @Param("startDate") String startDate,
                            @Param("endDate") String endDate,
                            @Param("offset") int offset,
                            @Param("size") int size);

    // ===== 数据看板统计 =====

    /** 员工总数 */
    @Select("SELECT COUNT(*) FROM emp")
    long countTotal();

    /** 按在职状态统计 */
    @Select("SELECT COUNT(*) FROM emp WHERE status = #{status}")
    long countByStatus(@Param("status") String status);

    /** 本月入职人数 */
    @Select("SELECT COUNT(*) FROM emp WHERE entry_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01') AND entry_date <= LAST_DAY(CURDATE())")
    long countNewHiresThisMonth();

    @Select("SELECT COUNT(*) FROM emp WHERE id=#{id} AND password=#{password}")
    int checkPassword(@Param("id") Integer id, @Param("password") String password);

    @Update("UPDATE emp SET password=#{password} WHERE id=#{id}")
    void updatePassword(@Param("id") Integer id, @Param("password") String password);
}