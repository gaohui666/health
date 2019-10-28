package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);      //添加方法
    Page<CheckItem> selectByCondition(String queryString);  //根据条件查询
    void deleteById(Integer id);        //根据id删除
    Long findCountByCheckItemId(int id);
    CheckItem findById(int id);     //根据id查询
    void edit(CheckItem checkItem);     //修改表单项
    List<CheckItem> findAll();      //查询所有表单项
}
