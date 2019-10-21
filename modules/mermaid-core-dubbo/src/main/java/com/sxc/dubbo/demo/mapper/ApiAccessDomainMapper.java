package com.sxc.dubbo.demo.mapper;

import com.sxc.dubbo.demo.module.ApiAccessDomain;
import com.sxc.dubbo.demo.module.ApiAccessDomainWithBLOBs;

public interface ApiAccessDomainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApiAccessDomainWithBLOBs record);

    int insertSelective(ApiAccessDomainWithBLOBs record);

    ApiAccessDomainWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApiAccessDomainWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ApiAccessDomainWithBLOBs record);

    int updateByPrimaryKey(ApiAccessDomain record);
}