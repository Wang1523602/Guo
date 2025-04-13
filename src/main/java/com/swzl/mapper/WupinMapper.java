package com.swzl.mapper;

import com.swzl.entity.Wupin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WupinMapper {
    //获取寻物列表
    List<Wupin> findXunWuList(@Param("startCount") int startCount, @Param("endCount") int endCount);

    //寻物模糊查询
    List<Wupin> findXunWuByWuPinName(String wupinmingcheng);

    //根据用户id查询寻物表
    List<Wupin> findXWByUserId(String userid);

    //根据用户id查询寻物表公示中的公告
    List<Wupin> findXWByUserIdWC(String userid);

    //寻物查询总记录数
    Integer findXunWuTotal();

    //寻物表按物品id查询详情
    Wupin findXunWuById(String id);

    //寻物表根据物品id删除某列
    void deleteXWById(String id);

    //插入寻物表
    void addXW(Wupin wupin);

    //管理员根据寻物表物品id改变该列信息状态
    void updateXWXZ(String id);

    //管理员获取全部寻物列表丢失中的公告
    List<Wupin> findAllXunWu();

    //管理员获取全部寻物列表已完成的公告
    List<Wupin> findAllXWWC();

    //    Integer searchWupinTotal(String wupinmingcheng);
    //获取招领列表
    List<Wupin> findZhaoLingList(@Param("startCount") int startCount, @Param("endCount") int endCount);

    //招领列表总记录数
    Integer findZhaoLingTotal();

    //招领表按id查询详情
    Wupin findZhaoLingById(String id);

    //根据用户id查询招领表公示中的公告
    List<Wupin> findZLByUserId(String userid);

    //根据用户id查询招领表已完成的公告
    List<Wupin> findZLByUserIdWC(String userid);

    //招领表根据物品id删除某列
    void deleteZLById(String id);

    // 招领列表查询某个站点已完成数量
     Integer findZhaoLingZDWCTotal(String bianhao);

    // 招领列表查询某个站点仍存放数量,即未完成数量
    Integer findZhaoLingZDCFTotal(String bianhao);

    //招领模糊查询
    List<Wupin> findZhaoLingByWuPinName(String wupinmingcheng);

    //插入招领表
    void addZL(Wupin wupin);

    //管理员根据招领表物品id改变该列信息状态
    void updateSWZL(String id);

    //插入寻物审核表
    void addXWSH(Wupin wupin);

    //根据用户id查询寻物审核表审核中的公告
    List<Wupin> findXWSHByUserId(String userid);

    //根据用户id查询寻物审核表被拒绝的公告
    List<Wupin> findXWSHByUserIdJJ(String userid);

    //根据物品id查询寻物审核表
    Wupin findXWSHById(String id);

    //根据物品id删除招领审核表某列
    void deleteZLSHById(String id);

    //管理员拒绝招领审核信息,根据id修改为拒绝
    void updateJJZLSH(String id);

    //根据物品id删除寻物审核表某列
    void deleteXWSHById(String id);

    //管理员显示全部寻物审核审核中列表
    List<Wupin> findAllXWSH();

    //管理员同意寻物审核信息,根据id修改为同意
    void updateXWSH(String id);

    //管理员拒绝寻物审核信息,根据id修改为拒绝
    void updateJJXWSH(String id);

    //插入招领审核表
    void addZLSH(Wupin wupin);

    //根据用户id查询招领审核表
    List<Wupin> findZLSHByUserId(String userid);

    //根据用户id查询招领审核表未通过公告
    List<Wupin> findZLSHByUserIdJJ(String userid);

    //根据物品id查询招领审核表
    Wupin findZLSHById(String id);

    //管理员显示全部招领审核中列表
    List<Wupin> findAllZLSH();

    //管理员同意招领审核信息,根据id修改为同意
    void updateZLSH(String id);

    //管理员显示全部招领公示中未领取列表
    List<Wupin> findAllSWZL();

    //管理员获取全部招领已完成的公告
    List<Wupin> findAllSWZLWC();

    //插入申领表
    void addSLSH(Wupin wupin);

    //根据用户id查询申领审核表
    List<Wupin> findSLSHByUserId(String userid);

    //根据用户id查询申领审核表正在进行的公告
    List<Wupin> findSLSHByUserIdJX(String userid);

    //根据用户id查询申领审核表已完成的公告
    List<Wupin> findSLSHByUserIdWC(String userid);

    //根据用户id查询申领审核表未通过的公告
    List<Wupin> findSLSHByUserIdJJ(String userid);

    //根据id查询申领审核表
    Wupin findSLSHById(String id);

    //根据id和申领用户查询申领审核表
    Wupin findSLSHByUserIdAndId(@Param("id") String id,@Param("userid") String userid);

    //根据物品id删除申领审核表某列
    void deleteSLSHById(String id);

    //管理员获取全部申领审核中的公告
    List<Wupin> findAllSLSH();

    //管理员同意申领审核信息,根据id修改为同意
    void updateSLSH(String id);

    //管理员拒绝申领审核信息,根据id修改为拒绝
    void updateJJSLSH(String id);

    //管理员获取全部申领进行中的公告
     List<Wupin> findAllSLSHJX();

    //管理员提交完成申领审核进行中信息,根据id修改为同意
    void updateSLSHWC(String id);

    //管理员获取全部申领已完成的公告
    List<Wupin> findAllSLSHWC();
}
