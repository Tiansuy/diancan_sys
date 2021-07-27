package com.tiansuy.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("item")
@Data
public class Item {
    @TableId(value = "item_id",type = IdType.AUTO)
    private Long item_id;
    private String food_name;
    private Long order_id;
    private Integer food_num;
    private Double item_price;
    private String item_state;

}
