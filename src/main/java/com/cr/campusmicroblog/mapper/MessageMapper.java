package com.cr.campusmicroblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cr.campusmicroblog.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author cr
 * @date 2020-11-17 17:54
 */
public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 查询当前用户站内信列表
     *
     * @param userId 用户id
     * @return 消息对象集合
     */
    @Select("SELECT ANY_VALUE(id)              id,\n" +
            "       ANY_VALUE(from_id)         from_id,\n" +
            "       ANY_VALUE(to_id)           to_id,\n" +
            "       ANY_VALUE(content)         content,\n" +
            "       ANY_VALUE(created_date)    created_date,\n" +
            "       ANY_VALUE(has_read)        has_read,\n" +
            "       ANY_VALUE(conversation_id) conversation_id,\n" +
            "       COUNT(*)                   cnt\n" +
            "FROM (SELECT *\n" +
            "      FROM message\n" +
            "      WHERE from_id = #{userId}\n" +
            "         OR to_id = #{userId}\n" +
            "      ORDER BY id DESC) t\n" +
            "GROUP BY conversation_id\n" +
            "ORDER BY id DESC")
    List<Message> getConversationList(@Param("userId") int userId);
}
