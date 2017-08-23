package com.bqjr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bqjr.model.EntityName;

public interface EntityNameMapper {
    int deleteByPrimaryKey(String SERIALNO);

    int insert(EntityName record);

    int insertSelective(EntityName record);

    List<EntityName> selectByPrimaryKey(@Param("SERIALNO")String SERIALNO);

    int updateByPrimaryKeySelective(EntityName record);

    int updateByPrimaryKey(EntityName record);
}