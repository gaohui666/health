package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.CheckItemService;
import com.itheima.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
检查管理
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_PERMISSION_SUCCESS);
        }
        return new Result(true, MessageConstant.ADD_PERMISSION_FAIL);
    }

    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
      return permissionService.pageQuery(queryPageBean);
    }

    //删除权限
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")   //权限校验
    @RequestMapping("delete")
    public Result delete(Integer id){
        try {
            permissionService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    //通过id回写数据
    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Permission permission = permissionService.findById(id);
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    //更新回写的数据
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

    //查询所有表单项
    @RequestMapping("/findAll")
    public Result findAll(){
        List<Permission> list = permissionService.findAll();
        if (list !=null && list.size()>0){
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,list);
        }else {
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

}
