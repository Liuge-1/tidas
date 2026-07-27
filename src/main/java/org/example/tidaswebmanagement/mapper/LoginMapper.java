package org.example.tidaswebmanagement.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.example.tidaswebmanagement.pojo.Emp;

import java.util.List;

@Mapper
public interface LoginMapper {

    @Select("SELECT id, username, name, role_code, status FROM emp WHERE username = #{username} AND password = #{password}")
    Emp login(String username, String password);

    // 检查用户名是否已存在
    @Select("SELECT COUNT(*) FROM emp WHERE username = #{username}")
    int countByUsername(String username);

    // 新增员工（注册，含邮箱，默认 role_code='emp'）
    @Insert("INSERT INTO emp (username, password, name, gender, phone, email, role_code, job, entry_date, create_time, update_time) " +
            "VALUES (#{username}, #{password}, #{name}, #{gender}, #{phone}, #{email}, 'emp', #{job}, CURDATE(), NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertEmp(Emp emp);

    // 查询已有职位列表（去重）
    @Select("SELECT DISTINCT job FROM emp WHERE job IS NOT NULL AND job != '' ORDER BY job")
    List<String> getDistinctJobs();

    // 根据 id 查用户名（供切面日志用）
    @Select("SELECT username FROM emp WHERE id = #{id}")
    String getUsernameById(Integer id);

    // ===== 邮箱验证码登录 / 密码重置 =====

    // 根据邮箱查用户
    @Select("SELECT id, username, name, role_code, status, email FROM emp WHERE email = #{email}")
    Emp findByEmail(String email);

    // 根据邮箱重置密码
    @org.apache.ibatis.annotations.Update("UPDATE emp SET password = #{password}, update_time = NOW(), version = version + 1 WHERE email = #{email}")
    int updatePasswordByEmail(@org.apache.ibatis.annotations.Param("email") String email,
                              @org.apache.ibatis.annotations.Param("password") String password);
}
