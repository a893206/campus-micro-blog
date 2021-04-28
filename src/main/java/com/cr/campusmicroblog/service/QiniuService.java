package com.cr.campusmicroblog.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author cr
 * @date 2020-11-17 13:57
 */
public interface QiniuService {
     String QINIU_IMAGE_DOMAIN = "http://qjvqzlg7v.hn-bkt.clouddn.com/";

    /**
     * 保存图片
     *
     * @param file 图片
     * @return 保存结果
     * @throws IOException IO异常
     */
    String saveImage(MultipartFile file) throws IOException;
}
