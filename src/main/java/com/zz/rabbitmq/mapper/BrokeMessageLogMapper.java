package com.zz.rabbitmq.mapper;


import com.zz.rabbitmq.entity.BrokeMessageLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BrokeMessageLogMapper {

    int deleteByPrimaryKey(String messageId);

    int insert(BrokeMessageLog record);

    int insertSelective(BrokeMessageLog record);

    BrokeMessageLog selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokeMessageLog record);

    int updateByPrimaryKey(BrokeMessageLog record);

    List<BrokeMessageLog> selectList();

    List<BrokeMessageLog> query4StatusAndTimeOutMessage();

    void update4ReSend(@Param("messageId") String messageId,@Param("updateTime") Date updateTime);

    void changeBrokeMessageLogStatus(@Param("messageId") String messageId, @Param("status") String status, @Param("updateTime") Date updateTime);

}