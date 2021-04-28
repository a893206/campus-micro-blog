package com.cr.campusmicroblog.async.handler;

import com.cr.campusmicroblog.async.EventHandler;
import com.cr.campusmicroblog.async.EventModel;
import com.cr.campusmicroblog.async.EventType;
import com.cr.campusmicroblog.entity.Message;
import com.cr.campusmicroblog.service.MessageService;
import com.cr.campusmicroblog.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author cr
 * @date 2020-11-19 11:57
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(1);
        message.setToId(model.getActorId());
        message.setContent("欢迎使用校园微博系统");
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(String.format("%d_%d", 1, model.getActorId()));

        messageService.addMessage(message);
        mailSender.sendWithHtmlTemplate(model.getExt("email"), "登录通知", "mails/welcome.html", null);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.LOGIN);
    }
}
