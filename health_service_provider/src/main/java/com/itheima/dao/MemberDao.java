package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {

    Member findByTelephone(String telephone);   //通过手机号查询数据库
    void add(Member member);    //会员注册

    Integer findMemberCountBeforeDate(String month);

    Integer findMemberCountByDate(String today);

    Integer findMemberTotalCount();


    Integer findMemberCountAfterDate(String thisWeekMonday);

    List<Map<String, Object>> findMemberBySex();

    List<Map<String, Object>> findMemberByAge();

    Integer findCountByAge1();

    Integer findCountByAge2();

    Integer findCountByAge3();

    Integer findCountByAge4();

    Integer findCountByAge5();
}
