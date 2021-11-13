package com.haoxu.highvolumeretailstoreappointmentsystem.db.mappers;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}