package com.manage.config;

import com.manage.model.comm.R;
import com.manage.service.IFileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2021/12/5 00:13
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {


    @Resource
    IFileUploadService fileUploadService;

    @PostMapping()
    public R doUpload(MultipartFile file, HttpServletRequest request) {
        String filePath = fileUploadService.uploadFile(file, request);
        Map<String, String> map = new HashMap<>();
        map.put("picUrl", filePath);
        return R.ok(map);
    }

}
