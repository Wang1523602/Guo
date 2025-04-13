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
public class Liuyan {

    private Integer id;
    private Integer userid;
    private String bianhao;
    private String liuyanneirong;

    private Date liuyanriqi;
    private String liuyanren;

    private Date addtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }


    public String getLiuyanneirong() {
        return liuyanneirong;
    }

    public void setLiuyanneirong(String liuyanneirong) {
        this.liuyanneirong = liuyanneirong;
    }

    public Date getLiuyanriqi() {
        return liuyanriqi;
    }

    public void setLiuyanriqi(Date liuyanriqi) {
        this.liuyanriqi = liuyanriqi;
    }

    public String getLiuyanren() {
        return liuyanren;
    }

    public void setLiuyanren(String liuyanren) {
        this.liuyanren = liuyanren;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "Liuyan{" +
                "id=" + id +
                ", userid=" + userid +
                ", bianhao='" + bianhao + '\'' +
                ", liuyanneirong='" + liuyanneirong + '\'' +
                ", liuyanriqi=" + liuyanriqi +
                ", liuyanren='" + liuyanren + '\'' +
                ", addtime=" + addtime +
                '}';
    }
}
