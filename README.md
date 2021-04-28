# 校园微博系统

## 涉及技术

SpringBoot、Spring、SpringMVC、MyBatis-Plus、七牛云、Redis、MySQL 8、Thymeleaf、Bootstrap

## 项目实现功能

1. 用户注册、登录

2. 图片上传及管理

3. 微博首页分页展示

4. 微博、评论发布

5. 站内信通知

6. 赞踩功能

7. 邮件通知

## 细节改进

1. 微博、评论、站内信分页使用PageHelper分页插件实现（设置页码数量navigatePages）、前端搭配Bootstrap调用PageInfo类的导航页数组navigatepageNums（默认为8，修改为5）使页码最多显示当前页码附近的5页。
2. 使用Bootstrap navbar-static-top样式使导航条静止在顶部，导航条添加站内信未读消息图标、当前用户头像、注销按钮。
3. 阅读站内信后清除未读数字。
4. 赞踩功能逻辑修改（likeCount = 点赞数 - 点踩数），编写前后端对应的代码，首先需要登录，未登录会弹出登录窗提示登录，点击后在添加或删除pressed样式的同时，修改Redis数据并使用Redis实现异步架构操作数据库。

## 初始化数据库

```mysql
DROP DATABASE IF EXISTS campus_micro_blog;

CREATE DATABASE campus_micro_blog;

USE campus_micro_blog;

DROP TABLE IF EXISTS mail;
CREATE TABLE mail
(
    id       INT AUTO_INCREMENT
        PRIMARY KEY,
    host     VARCHAR(64) NOT NULL,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(64) NULL,
    port     INT         NOT NULL,
    CONSTRAINT mail_username_uindex
        UNIQUE (username)
);

INSERT INTO campus_micro_blog.mail (id, host, username, password, port)
VALUES (1, 'smtp.qq.com', '请输入你的邮箱', '邮箱授权码', 465);
```

[init-schema.sql](https://github.com/a893206/campus-micro-blog/blob/master/src/test/resources/init-schema.sql)

[InitDatabaseTests](https://github.com/a893206/campus-micro-blog/blob/master/src/test/java/com/cr/toutiao/InitDatabaseTests.java)

运行InitDatabaseTests生成系统通知用户及测试数据。
