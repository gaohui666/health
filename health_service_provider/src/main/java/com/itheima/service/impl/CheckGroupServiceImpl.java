package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;
    @Override
    public void add(CheckGroup checkGroup,Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();   //得到当前页
        Integer pageSize = queryPageBean.getPageSize();         //得到每页的记录数
        PageHelper.startPage(currentPage,pageSize);             //调用分页插件
        String queryString = queryPageBean.getQueryString();    //调用参数对象中的条件属性
        Page<CheckGroup> page = checkGroupDao.findPage(queryString);    //通过条件查询得到page对象
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(int id) {
       CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }

    @Override
    public List<Integer> findItemIdsByGroupId(int id) {
       List<Integer> checkItems = checkGroupDao.findItemIdsByGroupId(id);
        return checkItems;
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.edit(checkGroup);     //修改基本表中的数据
        checkGroupDao.deleteAssocication(checkGroup.getId());  //切断中间表和选项组的联系
        Integer checkGroupId = checkGroup.getId();  //重新建立连接
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);

    }

    @Override
    public void delete(int id) {
        checkGroupDao.deleteAssocication(id);
        checkGroupDao.delete(id);   //通过表单组的id将表单组删除
       // checkGroupDao.deleteByGroupId(id);  //通过表单组id 将关联表删除
    }

    @Override
    public List<CheckGroup> findAll() {
       List<CheckGroup> checkGroupList = checkGroupDao.findAll();
        return checkGroupList;
    }

    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if (checkitemIds !=null && checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
