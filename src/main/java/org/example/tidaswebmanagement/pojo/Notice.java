package org.example.tidaswebmanagement.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("notice")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    @TableField("publisher_id")
    private Integer publisherId;
    @TableField("publisher_name")
    private String publisherName;
    @TableField("is_top")
    private Integer isTop;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
