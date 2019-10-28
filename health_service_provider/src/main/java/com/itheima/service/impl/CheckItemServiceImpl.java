package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public Page<CheckItem> selectByCondition(String queryString) {
        Page<CheckItem> checkItems = checkItemDao.selectByCondition(queryString);
        return checkItems;
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //获取查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        PageHelper.startPage(currentPage,pageSize); //分页插件助手
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString); //利用dao进行条件查询
        long total = page.getTotal();   //获取总记录数
        List<CheckItem> rows = page.getResult();  //获取当前页的数据
        return new PageResult(total,rows);
    }

    @Override
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
        Long count = checkItemDao.findCountByCheckItemId(id);
        if (count>0){
            //不允许删除
            new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public Long findCountByCheckItemId(int id) {
       return checkItemDao.findCountByCheckItemId(id);
    }

    @Override
    public CheckItem findById(int id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }


    public List<CheckItem> findAll() {
        List<CheckItem> itemList = checkItemDao.findAll();
        return itemList;
    }


}
