package com.swzl.service;

import com.swzl.entity.Liuyan;
import com.swzl.mapper.LiuyanMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiuyanService {
    @Autowired
    private LiuyanMapper liuyanMapper;

    //上传留言
    public void add(Liuyan liuyan) {
        liuyanMapper.add(liuyan);
    }

    //获取留言表信息
    public List<Liuyan> findLiuYanList(int startCount, int endCount) {
        return liuyanMapper.findLiuYanList(startCount, endCount);
    }

    public Integer findLiuYanTotal() {
        return liuyanMapper.findLiuYanTotal();
    }

    //根据用户id获取留言列表
    public List<Liuyan> findLYByUserId(String userid) {
        return liuyanMapper.findLYByUserId(userid);
    }

    //根据id删除留言列表
    public void deleteLYById(String id) {
        liuyanMapper.deleteLYById(id);
    }

    //获取全部留言
    public List<Liuyan> findAllLY(){
        return liuyanMapper.findAllLY();
    }

}
