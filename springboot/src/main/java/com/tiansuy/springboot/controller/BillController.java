package com.tiansuy.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiansuy.springboot.common.Result;
import com.tiansuy.springboot.entity.Bill;
import com.tiansuy.springboot.mapper.BillMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/bill")
public class BillController {
    @Resource
    BillMapper billMapper;

    @PostMapping("/add-bill")
    public Result<?> save(@RequestBody Bill bill){
        billMapper.insert(bill);
        return Result.success(bill.getBill_id());
    }

    @PutMapping
    public Result<?> update(@RequestBody Bill bill){
        billMapper.updateById(bill);
        return Result.success();
    }

    @DeleteMapping("/{bill_id}")
    public Result<?> del(@PathVariable Long bill_id){
        billMapper.deleteById(bill_id);
        return Result.success();
    }

    @GetMapping("/find-by-state")
    public Result<?> findPageByState(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Bill> wrapper = Wrappers.<Bill>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            Date da = new Date();
            String strDateFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String nowDate = sdf.format(da);
            wrapper.eq(Bill::getBill_state,search).apply("date_format(bill_date,'%Y-%m-%d') = '"+nowDate+"'");
        }
        Page<Bill> billPage = billMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(billPage);
    }

    @GetMapping("/find-unpay")
    public Result<?> findPageUnPay(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Bill> wrapper = Wrappers.<Bill>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            Date da = new Date();
            String strDateFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String nowDate = sdf.format(da);
            wrapper.ne(Bill::getBill_state,search).apply("date_format(bill_date,'%Y-%m-%d') = '"+nowDate+"'");
        }
        Page<Bill> billPage = billMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(billPage);
    }

    @GetMapping("/find-by-uploader")
    public Result<?> findPageByUploader(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Bill> wrapper = Wrappers.<Bill>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.eq(Bill::getUploader,search);
        }
        Page<Bill> billPage = billMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(billPage);
    }
}
