package org.example.tidaswebmanagement.controller;

import org.example.tidaswebmanagement.anno.OperationLog;
import org.example.tidaswebmanagement.pojo.Notice;
import org.example.tidaswebmanagement.pojo.PageResult;
import org.example.tidaswebmanagement.pojo.Result;
import org.example.tidaswebmanagement.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;

    @OperationLog("发布公告")
    @PostMapping
    public Result addNotice(@RequestBody Notice notice) {
        logger.info("发布公告: {}", notice.getTitle());
        noticeService.addNotice(notice);
        return Result.success();
    }

    @OperationLog("修改公告")
    @PutMapping
    public Result updateNotice(@RequestBody Notice notice) {
        logger.info("修改公告: id={}", notice.getId());
        noticeService.updateNotice(notice);
        return Result.success();
    }

    @OperationLog("删除公告")
    @DeleteMapping
    public Result deleteNotice(@RequestParam Integer id) {
        logger.info("删除公告: id={}", id);
        noticeService.deleteNotice(id);
        return Result.success();
    }

    @GetMapping("/list")
    public PageResult<Notice> listNotices(@RequestParam Integer page, @RequestParam Integer pageSize) {
        logger.info("查询公告列表: page={}, pageSize={}", page, pageSize);
        return noticeService.listNotices(page, pageSize);
    }

    @GetMapping("/top")
    public Result getTopNotices() {
        logger.info("获取置顶公告");
        List<Notice> list = noticeService.getTopNotices();
        return Result.success(list);
    }
}
