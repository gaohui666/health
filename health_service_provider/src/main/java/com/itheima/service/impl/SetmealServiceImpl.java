package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import jdk.nashorn.internal.objects.annotations.Constructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {


    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);    //将套餐对象添加到数据库中
        Integer setmealId = setmeal.getId();    //得到新添加进去的套餐对象的id
        setSetmealAndGroupIds(setmealId, checkgroupIds);//将套餐id和检查组id存入map集合中
        //将图片名称保存到redis集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
        //setmealDao.addBySetmealIdAndCheckGroupId(map);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();   //得到当前页的数据
        Integer pageSize = queryPageBean.getPageSize();     //得到每页展示的总记录数
        PageHelper.startPage(currentPage,pageSize);         //调用分页方法进行分页'
        String queryString = queryPageBean.getQueryString();    //得到查询条件
        Page<Setmeal> page = setmealDao.findPage(queryString);  //通过查询得到数据
        return new PageResult(page.getTotal(),page.getResult());//将数据库中的总记录数和条目数赋值给page对象
    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> setmealList = setmealDao.findAll();
        return setmealList;
    }

    @Override
    public Setmeal findById(int id) {   //通过id查询套餐中的所有信息
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {   //查询套餐的数量
        return setmealDao.findSetmealCount();
    }

    private void setSetmealAndGroupIds(Integer setmealId, Integer[] checkgroupIds) {
        HashMap<String, Integer> map = new HashMap<>(); //新建集合
        for (Integer checkgroupId : checkgroupIds) {    //遍历检查组的id
            map.put("setmeal_id",setmealId);
            map.put("checkgroup_id",checkgroupId);
            setmealDao.addBySetmealIdAndCheckGroupId(map);
        }
    }
}
