package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;

import java.util.List;

public interface RoleService {
    PageResult pageQuery(QueryPageBean queryPageBean);

    void add(Role role, Integer[] permissions);

    Role findById(int id);

    void edit(Role role, Integer[] permissions);

    void delete(int id);

    List<Integer> findPermissionIdsByRoleId(int id);
}
