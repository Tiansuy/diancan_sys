package com.tiansuy.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiansuy.springboot.common.Result;
import com.tiansuy.springboot.entity.Bill;
import com.tiansuy.springboot.entity.Item;
import com.tiansuy.springboot.mapper.BillMapper;
import com.tiansuy.springboot.mapper.ItemMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemCotroller {
    @Resource
    ItemMapper itemMapper;

    @Resource
    BillMapper billMapper;

    @PostMapping("/{table_num}/{uploader}")
    public Result<?> save(@PathVariable Integer table_num,
                          @PathVariable String uploader,
                          @RequestBody List<Item> itemList){
        // 创建bill对象，并insert
        Bill bill = new Bill();
        bill.setBill_date(new Date());
        Double total_price=0.0;
        for(Item item:itemList){
            total_price+=item.getItem_price();
        }
        bill.setBill_price(total_price);
        bill.setUploader(uploader);
        bill.setTable_number(table_num);
        billMapper.insert(bill);
        for(Item item:itemList){
            item.setOrder_id(bill.getBill_id());
        }
        // 依次insert item对象
        for(Item item:itemList){
            itemMapper.insert(item);
        }
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
