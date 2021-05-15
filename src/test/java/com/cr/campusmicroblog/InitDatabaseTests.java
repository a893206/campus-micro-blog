package com.cr.campusmicroblog;

import com.cr.campusmicroblog.entity.*;
import com.cr.campusmicroblog.mapper.LoginTicketMapper;
import com.cr.campusmicroblog.mapper.MessageMapper;
import com.cr.campusmicroblog.mapper.MicroBlogMapper;
import com.cr.campusmicroblog.mapper.UserMapper;
import com.cr.campusmicroblog.service.CommentService;
import com.cr.campusmicroblog.util.JedisAdapter;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql("/init-schema.sql")
class InitDatabaseTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MicroBlogMapper microBlogMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Test
    void initData() {
        jedisAdapter.getJedis().flushAll();

        Random random = new Random();
        int n = 60;

        User admin = new User();
        admin.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        admin.setName("系统管理员");
        admin.setPassword("4C36D3A89DFB9C9BADDEEA96BFF6260D");
        admin.setSalt("e71e0");
        userMapper.insert(admin);

        for (int i = 2; i <= n; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("e71e0");
            userMapper.insert(user);
            user.setPassword("4C36D3A89DFB9C9BADDEEA96BFF6260D");
            userMapper.updateById(user);

            LoginTicket ticket = new LoginTicket();
            ticket.setUserId(i);
            ticket.setTicket(String.format("TICKET%d", i));
            ticket.setExpired(new Date());
            ticket.setStatus(0);
            loginTicketMapper.insert(ticket);
            ticket.setStatus(2);
            loginTicketMapper.updateById(ticket);

            MicroBlog microBlog = new MicroBlog();
            microBlog.setCommentCount(n / 2);
            Date date = new Date();
            date.setTime(date.getTime() - 1000L * 3600 * (100 - i));
            microBlog.setCreatedDate(date);
            microBlog.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            microBlog.setLikeCount(0);
            microBlog.setUserId(i);
            microBlog.setTitle(String.format("TITLE{%d}", i));
            microBlog.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            microBlogMapper.insert(microBlog);

            // 给每条微博插入30条评论
            for (int j = 1; j <= n / 2; j++) {
                Comment comment = new Comment();
                comment.setUserId(random.nextInt(n) + 1);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
                comment.setContent("Comment" + j);
                comment.setEntityId(microBlog.getId());
                comment.setEntityType(EntityType.ENTITY_MICRO_BLOG);
                commentService.addComment(comment);
            }

            Message message = new Message();
            message.setFromId(1);
            message.setToId(i);
            message.setContent("unread");
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setConversationId(String.format("1_%d", i));
            messageMapper.insert(message);
        }

        Assert.assertEquals("4C36D3A89DFB9C9BADDEEA96BFF6260D", userMapper.selectById(1).getPassword());

        Assert.assertNotNull(loginTicketMapper.selectById(1));

        Assert.assertNotNull(microBlogMapper.selectById(1));

        Assert.assertNotNull(commentService.getCommentsByEntity(1, EntityType.ENTITY_MICRO_BLOG, 1, 1).getList().get(0));

        Assert.assertNotNull(messageMapper.selectById(1));

        Assert.assertNotNull(loginTicketMapper.selectById(3));

        User user = userMapper.selectById(3);
        user.setName("匿名用户");
        userMapper.updateById(user);
    }
}
