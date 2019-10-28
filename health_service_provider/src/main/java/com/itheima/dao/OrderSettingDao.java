package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    void add(OrderSetting orderSetting);    //添加数据
    void editNumberByOrderDate(OrderSetting orderSetting);  //通过日期修改数据
    long findCountByOrderDate(Date orderDate);  //查询日期看是否存在日期相同的信息
    List<OrderSetting> findByDate(Map map);
    OrderSetting findByOrderDate(Date date);     //通过日期查询预约信息

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
