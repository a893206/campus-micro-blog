package com.cr.campusmicroblog.controller;

import com.cr.campusmicroblog.entity.*;
import com.cr.campusmicroblog.service.LikeService;
import com.cr.campusmicroblog.service.MicroBlogService;
import com.cr.campusmicroblog.service.UserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-16 23:12
 */
@Slf4j
@Controller
public class HomeController {
    @Autowired
    private MicroBlogService microBlogService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    private List<List<ViewObject>> getMicroBlog(List<MicroBlog> microBlogList) {
        List<List<ViewObject>> vosList = new ArrayList<>();
        List<ViewObject> vos = new ArrayList<>();
        String prev = null;
        for (MicroBlog microBlog : microBlogList) {
            String curr = new SimpleDateFormat("yyyy-MM-dd").format(microBlog.getCreatedDate());
            if (prev != null && !prev.equals(curr)) {
                vosList.add(vos);
                vos = new ArrayList<>();
            }
            add(vos, microBlog);
            prev = curr;
        }
        if (!vos.isEmpty()) {
            vosList.add(vos);
        }
        return vosList;
    }

    private void add(List<ViewObject> vos, MicroBlog microBlog) {
        ViewObject vo = new ViewObject();
        vo.set("microBlog", microBlog);
        vo.set("user", userService.getUser(microBlog.getUserId()));
        User user = hostHolder.getUser();
        if (user != null) {
            vo.set("like", likeService.getLikeStatus(user.getId(), EntityType.ENTITY_MICRO_BLOG, microBlog.getId()));
        } else {
            vo.set("like", 0);
        }
        vos.add(vo);
    }

    @GetMapping({"/", "/index"})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop,
                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo<MicroBlog> pageInfo = microBlogService.getLatestMicroBlog(0, pageNum, pageSize);
        model.addAttribute("vosList", getMicroBlog(pageInfo.getList()));
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pop", pop);
        return "home";
    }

    @GetMapping("/user/{userId}")
    public String userIndex(Model model, @PathVariable("userId") int userId,
                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo<MicroBlog> pageInfo = microBlogService.getLatestMicroBlog(userId, pageNum, pageSize);
        model.addAttribute("vosList", getMicroBlog(pageInfo.getList()));
        model.addAttribute("pageInfo", pageInfo);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
