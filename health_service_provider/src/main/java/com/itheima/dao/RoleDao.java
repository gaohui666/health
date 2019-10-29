package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(Integer userId);

    List<Map> findByRoleId(Integer roleId);
}
