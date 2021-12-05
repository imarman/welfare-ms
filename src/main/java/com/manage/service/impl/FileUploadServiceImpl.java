package com.manage.service.impl;

import com.manage.common.BusinessException;
import com.manage.service.IFileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * @date 2021/12/5 00:16
 */
@Service
@Slf4j
public class FileUploadServiceImpl implements IFileUploadService {

    @Value("${upload.filePath}")
    public String filePath;

    @Override
    public String uploadFile(MultipartFile file, HttpServletRequest request) {
        SimpleDateFormat format = new SimpleDateFormat("/yyyy/MM/dd");
        String filePosition = filePath + format.format(new Date());
        File fileObj = new File(filePosition);
        if (!fileObj.exists()) {
            boolean flag = fileObj.mkdirs();
            if (!flag) {
                log.error("文件夹:{},创建失败", filePosition);
                throw new BusinessException("文件夹创建失败");
            }
        }
        String oldName = file.getOriginalFilename();
        Optional.ofNullable(oldName).orElseThrow(() -> new RuntimeException("请上传文件"));
        String newFilename = UUID.randomUUID() + oldName.substring(oldName.indexOf("."));
        try {
            file.transferTo(new File(filePosition, newFilename));
            String resPath = request.getScheme() +
                    "://" +
                    request.getServerName() +
                    ":" +
                    request.getServerPort() +
                    "/api/img/avatar" +
                    format.format(new Date()) +
                    "/" +
                    newFilename;
            log.info("文件上传成功, 路径:{}", resPath);
            return resPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
