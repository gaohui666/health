package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettiongService {
    void add(List<OrderSetting> data);   //批量导入到数据库中

    List<Map> findByDate(String date);  //查询预约信息

    void edit(OrderSetting orderSetting);   //预约设置
}
