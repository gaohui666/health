package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;


public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> list);

    List<Map<String, Object>> findMemberCountBySex();

    Map<String, Object> getMemberCountByAge();
}
