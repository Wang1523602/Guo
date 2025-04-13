package com.swzl.service;

import com.swzl.entity.Zhandian;
import com.swzl.mapper.ZhandianMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZhandainService {
    @Autowired
    private ZhandianMapper zhandianMapper;

    public Zhandian findByZhanDianName(String zhandianmingcheng) {
        return zhandianMapper.findByZhanDianName(zhandianmingcheng);
    }

    public List<Zhandian> findZhanDianList(@Param("startCount") int startCount, @Param("endCount") int endCount) {
        return zhandianMapper.findZhanDianList(startCount, endCount);
    }

    public Integer findZhanDianTotal() {
        return zhandianMapper.findZhanDianTotal();
    }

    public Zhandian findZhanDianById(String id) {
        return zhandianMapper.findZhanDianById(id);
    }

    //站点模糊查询
    public List<Zhandian> findZDByName(String zhandianmingcheng) {
        return zhandianMapper.findZDByName(zhandianmingcheng);
    }

    //插入新站点信息
    public void addZD(Zhandian zhandian) {
        zhandianMapper.addZD(zhandian);
    }

    //根据编号查询站点信息
    public Zhandian findZhanDianByBh(String bianhao) {
        return zhandianMapper.findZhanDianByBh(bianhao);
    }

    //获取全部站点列表信息
    public List<Zhandian> findAllZhanDian() {
        return zhandianMapper.findAllZhanDian();
    }

    //招领点表根据id删除某列
    public void deleteZLDById(String id){
        zhandianMapper.deleteZLDById(id);
    }

}
