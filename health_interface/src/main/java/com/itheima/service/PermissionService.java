package com.itheima.service;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;

import java.util.List;

public interface PermissionService {
    void add(Permission Permission);
    Page<Permission> selectByCondition(String queryString);  //根据条件查询
    PageResult pageQuery(QueryPageBean queryPageBean);
    void deleteById(Integer id);
    Long findCountByPermissionId(int id);
    Permission findById(int id);
    void edit(Permission Permission);
    List<Permission> findAll();
}
