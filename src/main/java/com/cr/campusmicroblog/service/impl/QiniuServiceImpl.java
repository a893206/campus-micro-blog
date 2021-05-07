package com.cr.campusmicroblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cr.campusmicroblog.service.QiniuService;
import com.cr.campusmicroblog.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author cr
 * @date 2020-11-17 13:58
 */
@Slf4j
@Service
public class QiniuServiceImpl implements QiniuService {
    /**
     * 设置好账号的ACCESS_KEY和SECRET_KEY
     */
    String ACCESS_KEY = "PALearkWgPK0gPXuNMtGteBC7Vec1gYEE3aumo80";
    String SECRET_KEY = "GfFm7fMFv-WWiGe9Ox2IDitMXUIS2BbWf5fmuq0a";
    /**
     * 要上传的空间
     */
    String bucketname = "campus-micro-blog";

    /**
     * 密钥配置
     */
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    /**
     * 创建上传对象
     */
    UploadManager uploadManager = new UploadManager(new Configuration(Region.huanan()));

    /**
     * 简单上传，使用默认策略，只需要设置上传的空间名就可以了
     */
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        try {
            if (file.getOriginalFilename() == null) {
                return null;
            }
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + fileExt;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //打印返回的信息
            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                log.error("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            log.error("七牛异常:" + e.getMessage());
            return null;
        }
    }
}
