package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}
