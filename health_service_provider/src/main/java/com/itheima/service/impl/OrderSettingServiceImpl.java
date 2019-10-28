package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettiongService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettiongService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettiongService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {
        if (list !=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count>0){
                    //已经进行了预约设置，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //未进行预约设置，直接添加到数据库中
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> findByDate(String date) {

        String begin = date +"-1";
        String end = date +"-31";
        Map<String,String> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        //调用dao查询日期范围
       List<OrderSetting> orderList = orderSettingDao.findByDate(map);
       List<Map> result = new ArrayList<>();
       if (orderList != null && orderList.size()>0){
           for (OrderSetting orderSetting : orderList) {
               Map<String, Integer> hashMap = new HashMap<>();
               hashMap.put("date",orderSetting.getOrderDate().getDate());
               hashMap.put("number",orderSetting.getNumber());
               hashMap.put("reservations",orderSetting.getReservations());
               result.add(hashMap);
           }
       }
       return result;
    }

    @Override
    public void edit(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count>0){
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }

    //定时清理数据库过期预约信息(31号)
    @Override
    public void clearOrderSetting(Date date) throws Exception {
        String dateLast4Month = DateUtils.parseDate2String(date);
        orderSettingDao.clearOrderSetting(dateLast4Month);
    }
}
