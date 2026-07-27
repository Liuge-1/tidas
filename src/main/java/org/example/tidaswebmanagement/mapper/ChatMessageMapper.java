package org.example.tidaswebmanagement.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ChatMessageMapper {

    @Insert("INSERT INTO chat_message (sender_id, receiver_id, content) VALUES (#{senderId}, #{receiverId}, #{content})")
    void save(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId, @Param("content") String content);

    @Select("SELECT m.*, e.name as sender_name FROM chat_message m LEFT JOIN emp e ON m.sender_id = e.id WHERE (sender_id = #{userId1} AND receiver_id = #{userId2}) OR (sender_id = #{userId2} AND receiver_id = #{userId1}) ORDER BY m.create_time ASC")
    List<Map<String, Object>> getHistory(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);

    @Update("UPDATE chat_message SET is_read = 1 WHERE sender_id = #{senderId} AND receiver_id = #{receiverId} AND is_read = 0")
    void markAsRead(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    @Select("SELECT COUNT(*) FROM chat_message WHERE sender_id = #{senderId} AND receiver_id = #{receiverId} AND is_read = 0")
    int countUnread(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);
}