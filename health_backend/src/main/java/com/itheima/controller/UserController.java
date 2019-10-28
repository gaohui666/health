package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当spring security完成认证后，会将当前的用户信息保存到框架提供的应用上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if (user !=null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }

    @RequestMapping("/loginByRole")
    public Result loginByRole(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user !=null){
            String username = user.getUsername();
            Map<String,Object> map = userService.loginByRole(username);//此集合为返回前端的集合
            Map<String,Object> maps = new HashMap<>();
            maps.put("menuList",map);
            return new Result(true,MessageConstant.LOGIN_SUCCESS,maps);
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }

}
