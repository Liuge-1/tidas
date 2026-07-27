package org.example.tidaswebmanagement.service;

import org.example.tidaswebmanagement.pojo.Notice;
import org.example.tidaswebmanagement.pojo.PageResult;

import java.util.List;

public interface NoticeService {
    void addNotice(Notice notice);

    void updateNotice(Notice notice);

    void deleteNotice(Integer id);

    PageResult<Notice> listNotices(Integer page, Integer pageSize);

    List<Notice> getTopNotices();
}
