package com.tiansuy.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {
    @TableId(value = "user_id",type = IdType.AUTO)
    private Long user_id;
    private String username;
    private String password;
    private String user_avator;
    private String user_type;

}
