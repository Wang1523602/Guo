package com.swzl.mapper;

import com.swzl.entity.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper {
    List<Notice> findNotice(@Param("startCount") int startCount, @Param("endCount") int endCount);
    Notice findNoticeById(String g_id);
    List<Notice> findAllNotice();
    void deleteGGById(String id);
    void addGG(Notice notice);
}
