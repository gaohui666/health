package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    //根据用户名查询数据库获得用户的所有信息，同时需要查询角色关联的权限信息
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//查询用户的基本信息，不包含用户的角色
        if (user == null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户id查询对应的角色
       Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色id查询关联的权限
           Set<Permission> permissions = permissionDao.findByRoleId(roleId);
           role.setPermissions(permissions);    //让角色关联权限
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    public List loginByRole(String username) {
        Integer roleId = userDao.loginByRole(username);  //查询用户角色表得到角色id
        Map<String,Object> maps = new HashMap<>();      //新建maps集合,将后台数据封装到次集合中
        List<Map> list = roleDao.findByRoleId(roleId);   //得到后台查询的数据
        List<Map> listData = new ArrayList();
        List<Map> lists = new ArrayList();
        for (Map map : list) {
            String icon = (String) map.get("icon"); //得到标记，可以区分是父表题还是子标题
            if (icon == null){
                lists.add(map);
            }else {
                String path = (String) map.get("path"); //得到path路径
                String title = (String) map.get("title");   //得到父标题
                maps.put("path",path);
                maps.put("title",title);
                maps.put("icon",icon);
            }
        }
        maps.put("children",lists);
        listData.add(maps);
        return listData;
    }
}
