package org.example.tidaswebmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.tidaswebmanagement.exception.BusinessException;
import org.example.tidaswebmanagement.mapper.NoticeMapper;
import org.example.tidaswebmanagement.pojo.Notice;
import org.example.tidaswebmanagement.pojo.PageResult;
import org.example.tidaswebmanagement.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public void addNotice(Notice notice) {
        notice.setCreateTime(LocalDateTime.now());
        noticeMapper.insert(notice);
    }

    @Override
    public void updateNotice(Notice notice) {
        Notice existing = noticeMapper.selectById(notice.getId());
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        notice.setUpdateTime(LocalDateTime.now());
        noticeMapper.updateById(notice);
    }

    @Override
    public void deleteNotice(Integer id) {
        Notice existing = noticeMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        noticeMapper.deleteById(id);
    }

    @Override
    public PageResult<Notice> listNotices(Integer pageNum, Integer pageSize) {
        Page<Notice> page = new Page<>(pageNum, pageSize);
        List<Notice> notices = noticeMapper.findByPage(page);
        return new PageResult<>(page.getTotal(), notices);
    }

    @Override
    public List<Notice> getTopNotices() {
        return noticeMapper.findTop3();
    }
}
