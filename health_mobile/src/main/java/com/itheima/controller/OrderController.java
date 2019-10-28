package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/*
体检预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    //在线预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        //从redis中获得保存好的验证码
        String validateCodeInReids = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        //将用户输入的验证码和从redis中查询的验证码进行对比
        if (validateCodeInReids !=null && validateCode !=null && validateCode.equals(validateCodeInReids)){
            //如果对比成功，调用服务完成业务
            map.put("orderType", Order.ORDERTYPE_WEIXIN);   //设置预约类型，分为微信预约，电话预约
           Result result = null;
            try {
                result = orderService.order(map);   //通过Dubbo远程调用服务实现在线预约处理
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()){
                //预约成功，可以为用户发送短信
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }else {
            //如果对比失败，返回结果给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Map findById(int id){
        try {
            Map map = orderService.findById(id);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
