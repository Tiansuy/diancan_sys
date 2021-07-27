package com.tiansuy.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiansuy.springboot.common.Result;
import com.tiansuy.springboot.entity.User;
import com.tiansuy.springboot.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, user.getPassword()).eq(User::getUser_type, user.getUser_type()));
        if (res == null) {
            return Result.error("-1", "用户名或密码错误");
        }
        return Result.success(res);
    }

    @PostMapping("/avator")
    public Result<?> uploadImg(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = "F://diancan_sys//springboot//src//main//resources//static//user//";
        File dest = new File(filePath+fileName);
        try {
            file.transferTo(dest);
        }catch (IOException e){
            return Result.error("500",e.getMessage());
        }
        String urlPath = "user/"+fileName;
        return Result.success(urlPath);
    }

    @PostMapping("/add-user")
    public Result<?> save(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping("/edit-password/{newPsw}")
    public Result<?> updatePassword(@PathVariable String newPsw,@RequestBody User user){
//        System.out.println(user);
//        System.out.println(newPsw);
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUser_id, user.getUser_id())
                .eq(User::getPassword,user.getPassword()));
        if (res == null) {
            return Result.error("-1", "密码错误");
        }
        user.setPassword(newPsw);
        userMapper.updateById(user);
        return Result.success();
    }

    @PutMapping("/edit-user")
    public Result<?> updateUser(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{user_id}")
    public Result<?> del(@PathVariable Long user_id){
        userMapper.deleteById(user_id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getUsername, search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

    @GetMapping("/find-by-id")
    public Result<?> findPageById(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getUser_id, search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }
}
