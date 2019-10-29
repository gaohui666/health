package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.PermissionService;
import com.itheima.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限管理
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return roleService.pageQuery(queryPageBean);
    }
    //查询所有权限的数据
    @RequestMapping("/findAll")
    public Result findAll(){
        List<Permission> permission;
        try {
            permission =  permissionService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,permission);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissions){
        try {
            roleService.add(role,permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
        return new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
    }
    @RequestMapping("/findById")
    public Result findById(int id){
        Role role;
        try {
            role = roleService.findById(id);  //查询当前表单组的信息
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,role);
    }
    //查询表单项中被选中的选项
    @RequestMapping("/findPermissionIdsByRoleId")
    public Result findPermissionIdsByRoleId(int id){
        List<Integer> permissions;
        try {
            permissions = roleService.findPermissionIdsByRoleId(id);//通过组的id得到当前项的信息
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permissions);
    }

    //编辑表单页面
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role,Integer[] permissions){
        try {
            roleService.edit(role,permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
    }

    //删除表单组和表单项数据
    @RequestMapping("/delete")
    public Result delete(int id){
        try {
            roleService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ROLE_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
    }
}
