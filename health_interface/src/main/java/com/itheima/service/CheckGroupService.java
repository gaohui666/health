package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);  //添加表单组

    PageResult findPage(QueryPageBean queryPageBean);   //分页查询

    CheckGroup findById(int id);    //查询检查组

    List<Integer> findItemIdsByGroupId(int id);   //查询表单项

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);//更新回写的数据

    void delete(int id);    //删除数据

    List<CheckGroup> findAll();
    //public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
}
