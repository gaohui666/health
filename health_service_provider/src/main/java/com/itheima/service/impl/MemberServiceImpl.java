package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {   //根据手机号查询会员
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {        //新增会员
        if (member.getPassword() !=null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer> lists = new ArrayList<>();
        for (String month : list) {
            month = month + ".31";  //格式:2019.04.31
            Integer count = memberDao.findMemberCountBeforeDate(month);
            lists.add(count);
        }
        return lists;
    }


    //根据男女查询会员数量
    @Override
    public List<Map<String, Object>> findMemberCountBySex() {
        return memberDao.findMemberBySex();
    }

    @Override
    public Map<String, Object> getMemberCountByAge() {
        //List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        Integer count1=memberDao.findCountByAge1();//0-18
        map.put("0-18岁",count1);
        Integer count2=memberDao.findCountByAge2();//18-30
        map.put("18-30岁",count2);
        Integer count3=memberDao.findCountByAge3();//30-45
        map.put("30-45岁",count3);
        Integer count4=memberDao.findCountByAge4();//45-60
        map.put("45-60岁",count4);
        Integer count5=memberDao.findCountByAge5();//60以上
        map.put("60岁以上",count5);


        return map;
        //return memberDao.findMemberByAge();

    }

}
