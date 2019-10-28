package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    Result order(Map map) throws Exception;  //将map集合中的数据信息添加到数据库中

    Map findById(int id) throws Exception;   //通过id查询预约成功的数据
}
