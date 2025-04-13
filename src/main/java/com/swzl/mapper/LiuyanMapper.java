package com.swzl.mapper;

import com.swzl.entity.Liuyan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiuyanMapper {
    //插入留言表
    void add(Liuyan liuyan);

    //获取留言列表
    List<Liuyan> findLiuYanList(@Param("startCount") int startCount, @Param("endCount") int endCount);

    //留言查询总记录数
    Integer findLiuYanTotal();

    //根据用户id获取留言列表
    List<Liuyan> findLYByUserId(String userid);

    //根据id删除留言列表
    void deleteLYById(String id);

    //获取全部留言
    List<Liuyan> findAllLY();
}
