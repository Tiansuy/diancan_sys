package com.tiansuy.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiansuy.springboot.common.Result;
import com.tiansuy.springboot.entity.FoodType;
import com.tiansuy.springboot.mapper.FoodTypeMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/food-type")
public class FoodTypeController {
    @Resource
    FoodTypeMapper foodTypeMapper;
    
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<FoodType> wrapper = Wrappers.<FoodType>lambdaQuery();
        Page<FoodType> foodTypePage = foodTypeMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(foodTypePage);
    }
}
