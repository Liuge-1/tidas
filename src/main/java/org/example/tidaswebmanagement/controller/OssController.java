package org.example.tidaswebmanagement.controller;

import org.example.tidaswebmanagement.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * 测试上传接口，携带员工姓名，文件名前缀为姓名_
     */
    @PostMapping("/oss/upload/text")
    public String uploadText(@RequestParam("file") MultipartFile file,
                             @RequestParam("name") String name) throws Exception {
        String url = ossService.uploadFile(file, name);
        return "上传成功！文件地址：" + url;
    }

    /**
     * 前端业务上传接口，携带员工姓名
     */
    @PostMapping("/oss/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("name") String name) throws Exception {
        return ossService.uploadFile(file, name);
    }
}