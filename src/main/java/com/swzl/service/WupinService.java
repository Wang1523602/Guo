package com.swzl.service;

import com.swzl.entity.Wupin;
import com.swzl.mapper.WupinMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WupinService {

    @Autowired
    private WupinMapper wupinMapper;


    //寻物总记录数
    public Integer findXunWuTotal() {
        return wupinMapper.findXunWuTotal();
    }

    //寻物模糊查询
    public List<Wupin> findXunWuByWuPinName(String wupinmingcheng) {
        return wupinMapper.findXunWuByWuPinName(wupinmingcheng);
    }

    //寻物按开始索引和结束索引条件查询列表数据
    public List<Wupin> findXunWuList(int startCount, int endCount) {
        return wupinMapper.findXunWuList(startCount, endCount);
    }

    //寻物表按id查询详情
    public Wupin findXunWuById(String id) {
        return wupinMapper.findXunWuById(id);
    }

    //根据用户id查询寻物表
    public List<Wupin> findXWByUserId(String userid) {
        return wupinMapper.findXWByUserId(userid);
    }

    //根据用户id查询寻物表公示中的公告
    public List<Wupin> findXWByUserIdWC(String userid) {
        return wupinMapper.findXWByUserIdWC(userid);
    }

    //寻物表根据物品id删除某列
    public void deleteXWById(String id) {
        wupinMapper.deleteXWById(id);
    }

    //插入寻物表
    public void addXW(Wupin wupin) {
        wupinMapper.addXW(wupin);
    }


    //招领按开始索引和结束索引条件查询列表数据
    public List<Wupin> findZhaoLingList(int startCount, int endCount) {
        return wupinMapper.findZhaoLingList(startCount, endCount);
    }

    //招领总记录数
    public Integer findZhaoLingTotal() {
        return wupinMapper.findZhaoLingTotal();
    }

    //招领表按id查询详情
    public Wupin findZhaoLingById(String id) {
        return wupinMapper.findZhaoLingById(id);
    }

    //招领表根据物品id删除某列
    public void deleteZLById(String id) {
        wupinMapper.deleteZLById(id);
    }

    //根据物品id删除招领审核表某列
    public void deleteZLSHById(String id) {
        wupinMapper.deleteZLSHById(id);
    }

    //根据用户id查询招领表
    public List<Wupin> findZLByUserId(String userid) {
        return wupinMapper.findZLByUserId(userid);
    }

    //根据用户id查询招领表已完成的公告
    public List<Wupin> findZLByUserIdWC(String userid) {
        return wupinMapper.findZLByUserIdWC(userid);
    }

    //根据用户id查询招领审核表未通过公告
    public List<Wupin> findZLSHByUserIdJJ(String userid) {
        return wupinMapper.findZLSHByUserIdJJ(userid);
    }

    //招领模糊查询
    public List<Wupin> findZhaoLingByWuPinName(String wupinmingcheng) {
        return wupinMapper.findZhaoLingByWuPinName(wupinmingcheng);
    }

    //插入招领表
    public void addZL(Wupin wupin) {
        wupinMapper.addZL(wupin);
    }

    //插入招领审核表
    public void addZLSH(Wupin wupin) {
        wupinMapper.addZLSH(wupin);
    }

    //根据用户id查询招领审核表
    public List<Wupin> findZLSHByUserId(String userid) {
        return wupinMapper.findZLSHByUserId(userid);
    }

    //根据用户id查询寻物审核表
    public List<Wupin> findXWSHByUserId(String userid) {
        return wupinMapper.findXWSHByUserId(userid);
    }

    //根据用户id查询寻物审核表被拒绝的公告
    public List<Wupin> findXWSHByUserIdJJ(String userid) {
        return wupinMapper.findXWSHByUserIdJJ(userid);
    }

    //根据物品id查询寻物审核表
    public Wupin findXWSHById(String id) {
        return wupinMapper.findXWSHById(id);
    }

    //插入寻物审核表
    public void addXWSH(Wupin wupin) {
        wupinMapper.addXWSH(wupin);
    }

    //根据物品id删除寻物审核某列
    public void deleteXWSHById(String id) {
        wupinMapper.deleteXWSHById(id);
    }


    //管理员显示全部寻物审核列表
    public List<Wupin> findAllXWSH() {
        return wupinMapper.findAllXWSH();
    }

    //管理员同意寻物审核信息,根据id修改为同意
    public void updateXWSH(String id) {
        wupinMapper.updateXWSH(id);
    }

    //管理员拒绝寻物审核信息,根据id修改为拒绝
    public void updateJJXWSH(String id) {
        wupinMapper.updateJJXWSH(id);
    }

    //管理员获取全部寻物列表丢失中的公告
    public List<Wupin> findAllXunWu() {
        return wupinMapper.findAllXunWu();
    }

    //管理员根据寻物表物品id改变该列信息状态
    public void updateXWXZ(String id) {
        wupinMapper.updateXWXZ(id);
    }

    //管理员获取全部寻物列表已完成的公告
    public List<Wupin> findAllXWWC() {
        return wupinMapper.findAllXWWC();
    }

    //根据物品id查询招领审核表
    public Wupin findZLSHById(String id) {
        return wupinMapper.findZLSHById(id);
    }

    //管理员显示全部招领审核列表
    public List<Wupin> findAllZLSH() {
        return wupinMapper.findAllZLSH();
    }

    //管理员同意招领审核信息,根据id修改为同意
    public void updateZLSH(String id) {
        wupinMapper.updateZLSH(id);
    }

    //管理员拒绝招领审核信息,根据id修改为拒绝
    public void updateJJZLSH(String id) {
        wupinMapper.updateJJZLSH(id);
    }

    //管理员显示全部招领公示中列表
    public List<Wupin> findAllSWZL() {
        return wupinMapper.findAllSWZL();
    }

    //管理员根据招领表物品id改变该列信息状态
    public void updateSWZL(String id) {
        wupinMapper.updateSWZL(id);
    }

    //管理员获取全部招领已完成的公告
    public List<Wupin> findAllSWZLWC() {
        return wupinMapper.findAllSWZLWC();
    }

    //插入申领表
    public void addSLSH(Wupin wupin) {
        wupinMapper.addSLSH(wupin);
    }

    //根据用户id查询申领审核表
    public List<Wupin> findSLSHByUserId(String userid) {
        return wupinMapper.findSLSHByUserId(userid);
    }

    //根据id查询申领审核表
    public Wupin findSLSHById(String id) {
        return wupinMapper.findSLSHById(id);
    }

    //根据用户id查询申领审核表正在进行的公告
    public List<Wupin> findSLSHByUserIdJX(String userid) {
        return wupinMapper.findSLSHByUserIdJX(userid);
    }

    //根据用户id查询申领审核表已完成的公告
    public List<Wupin> findSLSHByUserIdWC(String userid) {
        return wupinMapper.findSLSHByUserIdWC(userid);
    }

    //根据用户id查询申领审核表未通过的公告
    public List<Wupin> findSLSHByUserIdJJ(String userid) {
        return wupinMapper.findSLSHByUserIdJJ(userid);
    }

    //根据id和申领用户查询申领审核表
    public Wupin findSLSHByUserIdAndId(String id, String userid) {
        return wupinMapper.findSLSHByUserIdAndId(id, userid);
    }

    //根据物品id删除申领审核表某列
    public void deleteSLSHById(String id) {
        wupinMapper.deleteSLSHById(id);
    }

    //管理员获取全部申领审核中的公告
    public List<Wupin> findAllSLSH() {
        return wupinMapper.findAllSLSH();
    }

    //管理员同意申领审核信息,根据id修改为同意
    public void updateSLSH(String id) {
        wupinMapper.updateSLSH(id);
    }

    //管理员拒绝申领审核信息,根据id修改为拒绝
    public void updateJJSLSH(String id) {
        wupinMapper.updateJJSLSH(id);
    }

    //管理员获取全部申领进行中的公告
    public List<Wupin> findAllSLSHJX() {
        return wupinMapper.findAllSLSHJX();
    }

    //管理员提交完成申领审核进行中信息,根据id修改为同意
    public void updateSLSHWC(String id) {
        wupinMapper.updateSLSHWC(id);
    }

    //管理员获取全部申领已完成的公告
    public List<Wupin> findAllSLSHWC() {
        return wupinMapper.findAllSLSHWC();
    }

    // 招领列表查询某个站点已完成数量
    public Integer findZhaoLingZDWCTotal(String bianhao){
        return wupinMapper.findZhaoLingZDWCTotal(bianhao);
    }

    // 招领列表查询某个站点仍存放数量,即未完成数量
   public Integer findZhaoLingZDCFTotal(String bianhao){
       return wupinMapper.findZhaoLingZDCFTotal(bianhao);
    }

}
