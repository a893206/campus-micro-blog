package com.cr.campusmicroblog.async.handler;

import com.cr.campusmicroblog.async.EventHandler;
import com.cr.campusmicroblog.async.EventModel;
import com.cr.campusmicroblog.async.EventType;
import com.cr.campusmicroblog.entity.Message;
import com.cr.campusmicroblog.entity.MicroBlog;
import com.cr.campusmicroblog.entity.User;
import com.cr.campusmicroblog.service.MessageService;
import com.cr.campusmicroblog.service.MicroBlogService;
import com.cr.campusmicroblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-18 23:01
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private MicroBlogService microBlogService;

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(1);
        message.setToId(model.getEntityOwnerId());

        User user = userService.getUser(model.getActorId());
        int microBlogId = model.getEntityId();
        MicroBlog microBlog = microBlogService.getById(microBlogId);
        message.setContent("用户" + user.getName() + " " + model.getExt("msg") +
                ", <a href=\"/microBlog/" + microBlogId + "\">" + microBlog.getTitle() + "</a>");
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(String.format("%d_%d", 1, model.getEntityOwnerId()));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.LIKE);
    }
}
