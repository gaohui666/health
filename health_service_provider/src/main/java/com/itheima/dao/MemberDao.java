package com.itheima.dao;

import com.itheima.pojo.Member;

public interface MemberDao {

    Member findByTelephone(String telephone);   //通过手机号查询数据库
    void add(Member member);    //会员注册

    Integer findMemberCountBeforeDate(String month);

    Integer findMemberCountByDate(String today);

    Integer findMemberTotalCount();


    Integer findMemberCountAfterDate(String thisWeekMonday);
}
