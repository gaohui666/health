package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;


    @Override
    public Result order(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");   //从map集合中取出用户设置的日期
        Date date = DateUtils.parseString2Date(orderDate);//将字符串转换为日期格式
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);  //通过日期查询数据表，看查询日期是否为预约日期
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        if (orderSetting.getReservations() > orderSetting.getNumber()){
            //预约已满，不能预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3、检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);   //查询会员信息
        if (member != null){
            //4、判断是否是重复预约（同一个人，同一个预约日期，同一个套餐）
            Integer id = member.getId();    //得到当前会员的id
            String setmealId = (String) map.get("setmealId");//套餐id
            //将重复预约的三个条件封装为order对象
            Order order = new Order(id,date,null,null,Integer.parseInt(setmealId));
            List<Order> list = orderDao.findByCondition(order); //将查询的结果返回
            if (list !=null && list.size()>0){
                //已经预约完成，不能成功预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //5、当前用户不是会员，则先注册再添加
            member = new Member();
            member.setName((String) map.get("name"));   //给用户赋值
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//自动完成会员注册
        }

        //6、预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());  //设置会员id
        order.setOrderDate(DateUtils.parseString2Date(orderDate));//预约日期
        order.setOrderType((String) map.get("orderType"));  //预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO); //到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//套餐Id
        orderDao.add(order);
        orderSetting.setReservations(orderSetting.getReservations() + 1);   //设置预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);  //修改预约人数
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS,order.getId());
    }


    @Override
    public Map findById(int id) throws Exception {
       Map map = orderDao.findById(id);
       if (map !=null){
           //处理日期格式
           Date orderDate = (Date) map.get("orderDate");
           map.put("orderDate",DateUtils.parseDate2String(orderDate));
       }
        return map;
    }
}
