package com.itheima.jobs;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.RedisConstant;
import com.itheima.service.OrderSettiongService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/*
自定义job，实现定时清理垃圾图片
 */


public class ClearImgJob {
    @Reference
    private OrderSettiongService orderSettiongService;
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //根据redis中保存的两个set集合进行插值计算，获得垃圾图片名称合集
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set != null){
            for (String picName:set){
                //删除七牛云服务器上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                //从Redis集合中删除图片的名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
                System.out.println("自定义任务执行，清理垃圾图片："+picName);
            }
        }
    }
    public void clearOrderSetting() {
        //清理数据库信息 ,31号之前的信息，

        //获取每个月的最后一天
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH, 31);
        Date date = instance.getTime();
        try {
            orderSettiongService.clearOrderSetting(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
