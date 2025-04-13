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
public class Notice {
    private Integer g_id;
    private Integer g_userid;
    private String g_username;
    private String g_authority;
    private String g_title;
    private String g_content;
    // 日期格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date g_addTime;

    public Integer getG_id() {
        return g_id;
    }

    public void setG_id(Integer g_id) {
        this.g_id = g_id;
    }


    public Integer getG_userid() {
        return g_userid;
    }

    public void setG_userid(Integer g_userid) {
        this.g_userid = g_userid;
    }

    public String getG_username() {
        return g_username;
    }

    public void setG_username(String g_username) {
        this.g_username = g_username;
    }

    public String getG_authority() {
        return g_authority;
    }

    public void setG_authority(String g_authority) {
        this.g_authority = g_authority;
    }

    public String getG_title() {
        return g_title;
    }

    public void setG_title(String g_title) {
        this.g_title = g_title;
    }

    public String getG_content() {
        return g_content;
    }

    public void setG_content(String g_content) {
        this.g_content = g_content;
    }

    public Date getG_addTime() {
        return g_addTime;
    }

    public void setG_addTime(Date g_addTime) {
        this.g_addTime = g_addTime;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "g_id='" + g_id + '\'' +
                ", g_userid='" + g_userid + '\'' +
                ", g_username='" + g_username + '\'' +
                ", g_authority='" + g_authority + '\'' +
                ", g_title='" + g_title + '\'' +
                ", g_content='" + g_content + '\'' +
                ", g_addTime='" + g_addTime + '\'' +
                '}';
    }
}
