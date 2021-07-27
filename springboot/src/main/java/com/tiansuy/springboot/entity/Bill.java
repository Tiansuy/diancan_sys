package com.tiansuy.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@TableName("bill")
@Data
@ToString
public class Bill {
    @TableId(value = "bill_id",type = IdType.AUTO)
    private Long bill_id;
    private Double bill_price;
    private String uploader;
    private Integer table_number;
    private String bill_state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bill_date;

}
