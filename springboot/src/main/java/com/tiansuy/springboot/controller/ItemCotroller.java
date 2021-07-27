package com.tiansuy.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiansuy.springboot.common.Result;
import com.tiansuy.springboot.entity.Item;
import com.tiansuy.springboot.mapper.ItemMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/item")
public class ItemCotroller {
    @Resource
    ItemMapper itemMapper;

    @PostMapping
    public Result<?> save(@RequestBody Item item){
        itemMapper.insert(item);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Item> wrapper = Wrappers.<Item>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.eq(Item::getOrder_id,search);
        }
        Page<Item> itemPage = itemMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(itemPage);
    }
}
