package com.cr.campusmicroblog.controller;

import com.cr.campusmicroblog.entity.*;
import com.cr.campusmicroblog.service.*;
import com.cr.campusmicroblog.util.ToutiaoUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-17 13:52
 */
@Slf4j
@Controller
public class MicroBlogController {
    @Autowired
    private MicroBlogService microBlogService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            log.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }

    @GetMapping("/image")
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(QiniuService.QINIU_IMAGE_DOMAIN + imageName)), response.getOutputStream());
        } catch (Exception e) {
            log.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    @GetMapping("/microBlog/{microBlogId}")
    public String microBlogDetail(@PathVariable("microBlogId") int microBlogId, Model model,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        MicroBlog microBlog = microBlogService.getById(microBlogId);
        if (microBlog != null) {
            User user = hostHolder.getUser();
            if (user != null) {
                model.addAttribute("like", likeService.getLikeStatus(user.getId(), EntityType.ENTITY_MICRO_BLOG, microBlog.getId()));
            } else {
                model.addAttribute("like", 0);
            }
            //评论
            PageInfo<Comment> pageInfo = commentService.getCommentsByEntity(microBlogId, EntityType.ENTITY_MICRO_BLOG, pageNum, pageSize);
            List<Comment> commentList = pageInfo.getList();
            List<ViewObject> vos = new ArrayList<>();
            for (Comment comment : commentList) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("pageInfo", pageInfo);
        }
        model.addAttribute("microBlog", microBlog);
        model.addAttribute("owner", userService.getUser(microBlog.getUserId()));

        return "detail";
    }

    @PostMapping("/user/addMicroBlog")
    @ResponseBody
    public String addMicroBlog(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            MicroBlog microBlog = new MicroBlog();
            microBlog.setCreatedDate(new Date());
            microBlog.setTitle(title);
            microBlog.setImage(image);
            microBlog.setLink(link);
            microBlog.setLikeCount(0);
            microBlog.setCommentCount(0);
            if (hostHolder.getUser() != null) {
                microBlog.setUserId(hostHolder.getUser().getId());
            } else {
                // 设置一个匿名用户
                microBlog.setUserId(3);
            }
            microBlogService.addMicroBlog(microBlog);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            log.error("添加微博失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam("microBlogId") int microBlogId,
                             @RequestParam("content") String content) {

        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_MICRO_BLOG);
            comment.setEntityId(microBlogId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            microBlogService.updateCommentCount(comment.getEntityId(), count);

        } catch (Exception e) {
            log.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/microBlog/" + microBlogId;
    }
}
