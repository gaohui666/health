package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //获取查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        PageHelper.startPage(currentPage,pageSize); //分页插件助手
        Page<Role> page = roleDao.selectByCondition(queryString); //利用dao进行条件查询
        long total = page.getTotal();   //获取总记录数
        List<Role> rows = page.getResult();  //获取当前页的数据
        return new PageResult(total,rows);
    }

    //添加角色
    @Override
    public void add(Role role, Integer[] permissions) {
        roleDao.add(role);
        Integer roleId = role.getId();
        setRoleAndPermission(roleId,permissions);
    }

    //查询角色信息
    @Override
    public Role findById(int id) {
        Role role = roleDao.findById(id);
        return role;
    }

    //编辑角色信息
    @Override
    public void edit(Role role, Integer[] permissions) {
        roleDao.deleteAssocication(role.getId());//删除关联关系
        roleDao.edit(role);//修改表中基本数据
        Integer roleId = role.getId();
        setRoleAndPermission(roleId,permissions);


    }

    //删除角色的信息
    @Override
    public void delete(int id) {
        roleDao.deleteAssocication(id);//删除关联关系
        roleDao.delete(id);//删除角色信息
    }

    //回显角色权限选中情况
    @Override
    public List<Integer> findPermissionIdsByRoleId(int id) {
        List<Integer> permissionIds = roleDao.findPermissionIdsByRoleId(id);
        return permissionIds;
    }

    public void setRoleAndPermission(Integer roleId,Integer[] permissions){
        if (permissions !=null && permissions.length>0){
            for (Integer permissionId : permissions) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("role_id",roleId);
                map.put("permission_id",permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }


}
