package org.example.tidaswebmanagement.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    // 原有无参上传（兼容旧代码）
    String uploadFile(MultipartFile file) throws Exception;
    // 新增：传入姓名拼接文件名
    String uploadFile(MultipartFile file, String name) throws Exception;
}
