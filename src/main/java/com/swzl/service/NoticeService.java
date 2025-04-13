package com.swzl.service;

import com.swzl.entity.Notice;
import com.swzl.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public List<Notice> findNotice(int startCount, int endCount) {
        return noticeMapper.findNotice(startCount, endCount);
    }

    public Notice findNoticeById(String g_id) {
        return noticeMapper.findNoticeById(g_id);
    }

    public List<Notice> findAllNotice() {
        return noticeMapper.findAllNotice();
    }

    public void deleteGGById(String id){
        noticeMapper.deleteGGById(id);
    }

    public void addGG(Notice notice){
        noticeMapper.addGG(notice);
    }
}
