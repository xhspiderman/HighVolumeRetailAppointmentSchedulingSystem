package com.haoxu.highvolumeretailstoreappointmentsystem.db.dao;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.mappers.ShopMapper;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Shop;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ShopDaoImpl implements ShopDao {
    @Resource
    private ShopMapper shopMapper;

    @Override
    public Shop queryShopById(int shopId) {
        return shopMapper.selectByPrimaryKey(shopId);
    }
}
