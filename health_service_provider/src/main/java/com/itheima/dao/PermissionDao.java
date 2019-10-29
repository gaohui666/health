package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);

    void add(Permission permission);

    Page<Permission> selectByCondition(String queryString);

    long findCountByPermissionId(Integer id);

    void deleteById(Integer id);

    Permission findById(int id);

    void edit(Permission permission);


    //查询所有权限信息
    List<Permission> findAll();
}
