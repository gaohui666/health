package com.itheima.controller;

/*
登录验证
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        //1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        String validateCode = (String) map.get("validateCode"); //得到用户输入的校验码
        String telephone = (String) map.get("telephone");   //得到手机号
        String validateCodeByRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);//从redis中取出验证码
        if (validateCode ==null && telephone ==null && !validateCode.equals(validateCodeByRedis)){//验证码不一致
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
            //2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
           Member member = memberService.findByTelephone(telephone);    //通过手机号查询用户是否存在
            if (member == null){    //当前用户不是会员，自动zhuc
                member=new Member();
                member.setPhoneNumber(telephone);   //存入电话号
                member.setRegTime(new Date());      //存入注册时间
                memberService.add(member);
            }
            //登录成功，向客户端写入Cookie，内容为用户手机号
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");//路径，webapp下的所有应用共享cookie
            cookie.setMaxAge(60*60*24*30);//路径有效期30天
            response.addCookie(cookie);
            //将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }

}
