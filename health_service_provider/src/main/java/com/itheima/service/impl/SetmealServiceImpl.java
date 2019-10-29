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
import com.itheima.utils.SerializeUtils;
import jdk.nashorn.internal.objects.annotations.Constructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.*;

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
        jedisPool.getResource().sadd("setmealList".getBytes(),SerializeUtils.serialize(setmeal));//将套餐对象添加到redis中
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
        List<Setmeal> setmealList = new ArrayList<>();
        //从jedis数据库中进行查询
        Set<byte[]> smembers = jedisPool.getResource().smembers("setmealList".getBytes());
        if (smembers !=null && smembers.size()>0) {
            //将查询到的数据存入setmealList集合
            for (byte[] smember : smembers) {
                Setmeal setmeal = (Setmeal) SerializeUtils.unserialize(smember);
                setmealList.add(setmeal);
            }
        }else {
            //如果查询为空
            //调用setmealDao从数据库中查询
            setmealList = setmealDao.findAll();
        //并将查询到的数据存储到redis集合中
            for (Setmeal setmeal : setmealList) {
                jedisPool.getResource().sadd("setmealList".getBytes(), SerializeUtils.serialize(setmeal));
            }
        }
        return setmealList;
    }

    @Override
    public Setmeal findById(int id) {   //通过id查询套餐中的所有信息
        Setmeal setmeal = new Setmeal();
        //从jedis数据库中进行查询
        Set<byte[]> smembers = jedisPool.getResource().smembers("setmeal".getBytes());
        boolean flag = false;
        if (smembers !=null && smembers.size()>0) {
            for (byte[] smember : smembers) {
                Setmeal setmeal1 = (Setmeal) SerializeUtils.unserialize(smember);
                if (setmeal1.getId() == id) {
                    flag = true;
                    setmeal = setmeal1;
                    return setmeal;
                }
            }
        }

        if (flag == false||smembers == null||smembers.size() == 0){
            //从数据库中进行查询,并将查询结果存储在redis中
             setmeal = setmealDao.findById(id);
             jedisPool.getResource().sadd("setmeal".getBytes(),SerializeUtils.serialize(setmeal));
        }
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
