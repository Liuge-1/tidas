package org.example.tidaswebmanagement.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
public class Emp {
    @TableId(type = IdType.AUTO)
    private Integer id; //ID,主键
    private String username; //用户名
    private String password; //密码
    private String name; //姓名
    private String gender; //性别，1：男，2：女
    private String phone; //手机号
    private String email; //邮箱
    @TableField("role_code")
    private String roleCode; //角色：admin管理员 / emp员工
    private String job; //职位，1:班主任,2:讲师,3:学工主管,4:教研主管,5:咨询师
    private String status; //在职状态：在职/离职
    private Integer salary; //薪资
    private String image; //头像
    @TableField("entry_date")
    private LocalDate entryDate; //入职日期
    @TableField("dept_id")
    private Integer deptId; //关联的部门ID
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime; //修改时间
    private Integer version; //乐观锁版本号（由 XML mapper 手动管理）
    private String deptName;


    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }

    public void setStatus(String status) { this.status = status; }

    public void setJob(String job) {
        this.job = job;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() { return version; }

    public void setVersion(Integer version) { this.version = version; }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Emp(String username, String name, String gender, String phone, String job, Integer salary, String image, LocalDate entryDate, Integer deptId) {
        //username, name, gender, phone, job, salary, image, entrydate, deptid

        this.username = username;

        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.job = job;
        this.salary = salary;
        this.image = image;
        this.entryDate = entryDate;
        this.deptId = deptId;

    }


    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleCode() { return roleCode; }

    public String getStatus() { return status; }

    public String getJob() {
        return job;
    }

    public Integer getSalary() {
        return salary;
    }

    public String getImage() {
        return image;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public String getDeptName() {
        return deptName;
    }


}
