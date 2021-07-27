package com.tiansuy.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiansuy.springboot.common.Result;
import com.tiansuy.springboot.entity.Notice;
import com.tiansuy.springboot.mapper.NoticeMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    NoticeMapper noticeMapper;

    @PostMapping("/add-notice")
    public Result<?> save(@RequestBody Notice notice){
        noticeMapper.insert(notice);
        return Result.success();
    }

    @PutMapping("/edit-notice")
    public Result<?> update(@RequestBody Notice notice){
        noticeMapper.updateById(notice);
        return Result.success();
    }

    @DeleteMapping("/{notice_id}")
    public Result<?> del(@PathVariable Long notice_id){
        noticeMapper.deleteById(notice_id);
        return Result.success();
    }

    @RequestMapping("/find-by-uploader")
    public Result<?> findPageByUploader(@RequestParam(defaultValue = "1") Integer pageNumber,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Notice> wrapper = Wrappers.<Notice>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Notice::getUploader,search);
        }
        Page<Notice> noticePage = noticeMapper.selectPage(new Page<>(pageNumber, pageSize), wrapper);
        return Result.success(noticePage);
    }

    @RequestMapping("/find-by-state")
    public Result<?> findPageByState(@RequestParam(defaultValue = "1") Integer pageNumber,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Notice> wrapper = Wrappers.<Notice>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Notice::getNotice_state,search);
        }
        Page<Notice> noticePage = noticeMapper.selectPage(new Page<>(pageNumber, pageSize), wrapper);
        return Result.success(noticePage);
    }
}
