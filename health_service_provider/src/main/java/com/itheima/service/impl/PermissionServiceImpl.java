package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override//添加用户权限
    public void add(Permission Permission) {

        permissionDao.add(Permission);

    }

    @Override//根据条件查询
    public Page<Permission> selectByCondition(String queryString) {
        Page<Permission> permissionPage = permissionDao.selectByCondition(queryString);
        return permissionPage;
    }

    @Override//分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //获取查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        PageHelper.startPage(currentPage, pageSize); //分页插件助手
        Page<Permission> page = permissionDao.selectByCondition(queryString); //利用dao进行条件查询
        long total = page.getTotal();   //获取总记录数
        List<Permission> rows = page.getResult();  //获取当前页的数据
        return new PageResult(total, rows);

    }

    @Override//通过id删除权限
    public void deleteById(Integer id) {

        long count = permissionDao.findCountByPermissionId(id);
        if (count>0){
            //不允许删除
           new RuntimeException();
        }
        permissionDao.deleteById(id);
    }

    @Override//判断权限是否关联角色
    public Long findCountByPermissionId(int id) {
        return permissionDao.findCountByPermissionId(id);
    }

    @Override//通过id查询(回显)
    public Permission findById(int id) {
        return permissionDao.findById(id);
    }

    @Override//更新数据
    public void edit(Permission Permission) {
        permissionDao.edit(Permission);
    }

    @Override//查询所有权限
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }
}
