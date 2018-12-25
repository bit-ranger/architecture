package com.rainyalley.architecture.mapper;

import com.rainyalley.architecture.entity.ReentrantLockEntity;
import com.rainyalley.architecture.entity.ReentrantLockEntityExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReentrantLockEntityMapper {
    long countByExample(ReentrantLockEntityExample example);

    int deleteByExample(ReentrantLockEntityExample example);

    int deleteByPrimaryKey(String target);

    int insert(ReentrantLockEntity record);

    int insertSelective(ReentrantLockEntity record);

    List<ReentrantLockEntity> selectByExample(ReentrantLockEntityExample example);

    ReentrantLockEntity selectByPrimaryKey(String target);

    int updateByExampleSelective(@Param("record") ReentrantLockEntity record, @Param("example") ReentrantLockEntityExample example);

    int updateByExample(@Param("record") ReentrantLockEntity record, @Param("example") ReentrantLockEntityExample example);

    int updateByPrimaryKeySelective(ReentrantLockEntity record);

    int updateByPrimaryKey(ReentrantLockEntity record);
}