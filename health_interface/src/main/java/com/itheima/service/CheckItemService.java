package com.itheima.service;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    void add(CheckItem checkItem);
    Page<CheckItem> selectByCondition(String queryString);  //根据条件查询
    PageResult pageQuery(QueryPageBean queryPageBean);
    void deleteById(Integer id);
    Long findCountByCheckItemId(int id);
    CheckItem findById(int id);
    void edit(CheckItem checkItem);
    List<CheckItem> findAll();
}
