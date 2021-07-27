package com.tiansuy.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("notice")
@Data
public class Notice {
    @TableId(value = "notice_id",type = IdType.AUTO)
    private Long notice_id;
    private String notice_content;
    private String uploader;
    private String notice_state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date notice_date;

}
