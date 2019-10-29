package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(Integer userId);

    List<Map> findByRoleId(Integer roleId);

    List findByParentMenuId(Integer parentMenuId);

    Map<String, Object> findParentMenu(String icon);


    //分页查询
    Page<Role> selectByCondition(String queryString);

    void add(Role role);

    Role findById(int id);

    void deleteAssocication(Integer id);

    void edit(Role role);

    void delete(int id);

    List<Integer> findPermissionIdsByRoleId(int id);

    void setRoleAndPermission(HashMap<String, Integer> map);
}
