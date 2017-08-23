package com.bqjr.dao;

import com.bqjr.model.DkUserMq;
import com.bqjr.model.DkUserMqExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DkUserMqMapper {
    int countByExample(DkUserMqExample example);

    int deleteByExample(DkUserMqExample example);

    int deleteByPrimaryKey(String serialno);

    int insert(DkUserMq record);

    int insertSelective(DkUserMq record);

    List<DkUserMq> selectByExample(DkUserMqExample example);

    DkUserMq selectByPrimaryKey(String serialno);

    int updateByExampleSelective(@Param("record") DkUserMq record, @Param("example") DkUserMqExample example);

    int updateByExample(@Param("record") DkUserMq record, @Param("example") DkUserMqExample example);

    int updateByPrimaryKeySelective(DkUserMq record);

    int updateByPrimaryKey(DkUserMq record);
}