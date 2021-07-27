package com.tiansuy.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiansuy.springboot.common.Result;
import com.tiansuy.springboot.entity.Food;
import com.tiansuy.springboot.mapper.FoodMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/food")
public class FoodController {
    @Resource
    FoodMapper foodMapper;

    @PostMapping("/upload-img")
    public Result<?> uploadImg(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = "F://diancan_sys//springboot//src//main//resources//static//food//";
        File dest = new File(filePath+fileName);
        try {
            file.transferTo(dest);
        }catch (IOException e){
            return Result.error("500",e.getMessage());
        }
        String urlPath = "food/"+fileName;
        return Result.success(urlPath);
    }

    @PostMapping("/add-food")
    public Result<?> save(@RequestBody Food food){
        foodMapper.insert(food);
        return Result.success();
    }

    @PutMapping("/edit-food")
    public Result<?> update(@RequestBody Food food){
        foodMapper.updateById(food);
        return Result.success();
    }
    @DeleteMapping("/{food_id}")
    public Result<?> del(@PathVariable Long food_id){
        foodMapper.deleteById(food_id);
        return Result.success();
    }
    
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Food> wrapper = Wrappers.<Food>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Food::getFood_name, search);
        }
        Page<Food> foodPage = foodMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(foodPage);
    }
}
