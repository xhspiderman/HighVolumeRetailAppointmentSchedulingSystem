package com.haoxu.highvolumeretailstoreappointmentsystem.db.mappers;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Shop;

public interface ShopMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);
}