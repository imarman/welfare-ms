package com.manage.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @date 2021/12/5 00:15
 */
public interface IFileUploadService {

    /**
     * 文件上传
     * @param file 文件兑现
     * @return 地址
     */
    String uploadFile(MultipartFile file, HttpServletRequest request);

}
