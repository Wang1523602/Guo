package com.swzl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;

/*@Data
@AllArgsConstructor
@NoArgsConstructor*/
@Repository
public class Wupin {


    private Integer id;
    private String userid;
    private String bianhao;
    private String wupinmingcheng;
    private String tupian;
    private String shiquren;
    private String lianxidianhua;
    // 日期格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date shiquriqi;
    // 日期格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date jiandaoriqi;
    private String shiqudizhi;
    private String jiandaodizhi;
    private String jianshu;
    private String zhandianmingcheng;
    // 日期格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date daozhandianriqi;
    private String fuzeren;
    private String dizhi;
    private String zhuangtai;
    // 日期格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date addtime;
    private String shenhejieguo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public String getZhandianmingcheng() {
        return zhandianmingcheng;
    }

    public void setZhandianmingcheng(String zhandianmingcheng) {
        this.zhandianmingcheng = zhandianmingcheng;
    }

    public String getWupinmingcheng() {
        return wupinmingcheng;
    }

    public void setWupinmingcheng(String wupinmingcheng) {
        this.wupinmingcheng = wupinmingcheng;
    }

    public String getTupian() {
        return tupian;
    }

    public void setTupian(String tupian) {
        this.tupian = tupian;
    }

    public String getJianshu() {
        return jianshu;
    }

    public void setJianshu(String jianshu) {
        this.jianshu = jianshu;
    }

    public Date getDaozhandianriqi() {
        return daozhandianriqi;
    }

    public void setDaozhandianriqi(Date daozhandianriqi) {
        this.daozhandianriqi = daozhandianriqi;
    }

    public String getFuzeren() {
        return fuzeren;
    }

    public void setFuzeren(String fuzeren) {
        this.fuzeren = fuzeren;
    }

    public String getLianxidianhua() {
        return lianxidianhua;
    }

    public void setLianxidianhua(String lianxidianhua) {
        this.lianxidianhua = lianxidianhua;
    }

    public String getDizhi() {
        return dizhi;
    }

    public void setDizhi(String dizhi) {
        this.dizhi = dizhi;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getShiquren() {
        return shiquren;
    }

    public void setShiquren(String shiquren) {
        this.shiquren = shiquren;
    }

    public Date getShiquriqi() {
        return shiquriqi;
    }

    public void setShiquriqi(Date shiquriqi) {
        this.shiquriqi = shiquriqi;
    }

    public Date getJiandaoriqi() {
        return jiandaoriqi;
    }

    public void setJiandaoriqi(Date jiandaoriqi) {
        this.jiandaoriqi = jiandaoriqi;
    }

    public String getShiqudizhi() {
        return shiqudizhi;
    }

    public void setShiqudizhi(String shiqudizhi) {
        this.shiqudizhi = shiqudizhi;
    }

    public String getJiandaodizhi() {
        return jiandaodizhi;
    }

    public void setJiandaodizhi(String jiandaodizhi) {
        this.jiandaodizhi = jiandaodizhi;
    }

    public String getShenhejieguo() {
        return shenhejieguo;
    }

    public void setShenhejieguo(String shenhejieguo) {
        this.shenhejieguo = shenhejieguo;
    }

    @Override
    public String toString() {
        return "Wupin{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", bianhao='" + bianhao + '\'' +
                ", wupinmingcheng='" + wupinmingcheng + '\'' +
                ", tupian='" + tupian + '\'' +
                ", shiquren='" + shiquren + '\'' +
                ", lianxidianhua='" + lianxidianhua + '\'' +
                ", shiquriqi=" + shiquriqi +
                ", jiandaoriqi=" + jiandaoriqi +
                ", shiqudizhi='" + shiqudizhi + '\'' +
                ", jiandaodizhi='" + jiandaodizhi + '\'' +
                ", jianshu='" + jianshu + '\'' +
                ", zhandianmingcheng='" + zhandianmingcheng + '\'' +
                ", daozhandianriqi='" + daozhandianriqi + '\'' +
                ", fuzeren='" + fuzeren + '\'' +
                ", dizhi='" + dizhi + '\'' +
                ", zhuangtai='" + zhuangtai + '\'' +
                ", addtime=" + addtime +
                ", shenhejieguo='" + shenhejieguo + '\'' +
                '}';
    }
}
