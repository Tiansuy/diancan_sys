package com.tiansuy.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("food_type")
@Data
public class FoodType {
    @TableId(value = "food_type_id",type = IdType.AUTO)
    private Long food_type_id;
    private String food_type_name;
}
