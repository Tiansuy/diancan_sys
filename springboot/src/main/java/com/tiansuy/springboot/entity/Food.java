package com.tiansuy.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("food")
@Data
public class Food {
    @TableId(value = "food_id",type = IdType.AUTO)
    private Long food_id;
    private Double food_price;
    private String food_desc;
    private String food_name;
    private String food_img;
    private String food_type;
}
