package org.example.tidaswebmanagement.controller;

import org.example.tidaswebmanagement.mapper.ChatMessageMapper;
import org.example.tidaswebmanagement.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @GetMapping("/history")
    public Result getHistory(@RequestParam Integer userId1, @RequestParam Integer userId2) {
        List<Map<String, Object>> list = chatMessageMapper.getHistory(userId1, userId2);
        return Result.success(list);
    }

    @PostMapping("/save")
    public Result save(@RequestBody Map<String, Object> body) {
        Integer senderId = Integer.valueOf(body.get("senderId").toString());
        Integer receiverId = Integer.valueOf(body.get("receiverId").toString());
        String content = (String) body.get("content");
        chatMessageMapper.save(senderId, receiverId, content);
        return Result.success();
    }

    @GetMapping("/unread")
    public Result getUnread(@RequestParam Integer userId1, @RequestParam Integer userId2) {
        // 将 userId2 发给 userId1 的未读消息标记为已读，并返回之前的未读数
        int count = chatMessageMapper.countUnread(userId2, userId1);
        chatMessageMapper.markAsRead(userId2, userId1);
        return Result.success(count);
    }
}