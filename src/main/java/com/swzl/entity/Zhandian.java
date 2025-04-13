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
public class Zhandian {
    private Integer id;
    private String bianhao;
    private String zhandianmingcheng;
    private String jianjie;
    private String fuzeren;
    private String lianxidianhua;
    private String youxiang;
    private String dizhi;
    private String fuwushijian;
    // 日期格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date addtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
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

    public String getYouxiang() {
        return youxiang;
    }

    public void setYouxiang(String youxiang) {
        this.youxiang = youxiang;
    }

    public String getDizhi() {
        return dizhi;
    }

    public void setDizhi(String dizhi) {
        this.dizhi = dizhi;
    }

    public String getFuwushijian() {
        return fuwushijian;
    }

    public void setFuwushijian(String fuwushijian) {
        this.fuwushijian = fuwushijian;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "Zhandian{" +
                "id=" + id +
                ", bianhao='" + bianhao + '\'' +
                ", zhandianmingcheng='" + zhandianmingcheng + '\'' +
                ", jianjie='" + jianjie + '\'' +
                ", fuzeren='" + fuzeren + '\'' +
                ", lianxidianhua='" + lianxidianhua + '\'' +
                ", youxiang='" + youxiang + '\'' +
                ", dizhi='" + dizhi + '\'' +
                ", fuwushijian='" + fuwushijian + '\'' +
                ", addtime=" + addtime +
                '}';
    }
}
