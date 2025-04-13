package com.swzl.mapper;

import com.swzl.entity.Liuyan;
import com.swzl.entity.Zhandian;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZhandianMapper {
    //根据站点名称查询信息
    Zhandian findByZhanDianName( String zhandianmingcheng);
    //获取站点列表信息
    List<Zhandian> findZhanDianList(@Param("startCount") int startCount, @Param("endCount") int endCount);
    //站点总记录数
    Integer findZhanDianTotal();
    //根据id查询站点信息
    Zhandian findZhanDianById(String id);
    //站点模糊查询
    List<Zhandian> findZDByName(String zhandianmingcheng);
    //插入新站点信息
    void addZD(Zhandian zhandian);
    //根据编号查询站点信息
    Zhandian findZhanDianByBh(String bianhao);
    //获取全部站点列表信息
    List<Zhandian> findAllZhanDian();
    //招领点表根据id删除某列
    void deleteZLDById(String id);
}
