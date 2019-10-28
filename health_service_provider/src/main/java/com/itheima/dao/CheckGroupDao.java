package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.HashMap;
import java.util.List;

public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(HashMap<String, Integer> map);

    Page<CheckGroup> findPage(String queryString);
    CheckGroup findById(int id);

    List<Integer> findItemIdsByGroupId(int id);

    void edit(CheckGroup checkGroup);

    void deleteAssocication(Integer id);

    void delete(int id);

    void deleteByGroupId(int id);

    List<CheckGroup> findAll();

}
