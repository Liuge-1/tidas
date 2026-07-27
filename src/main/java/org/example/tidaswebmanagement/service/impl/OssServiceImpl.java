package org.example.tidaswebmanagement.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import org.example.tidaswebmanagement.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    // 注入旧版OSS客户端
    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * 接收前端上传文件，上传OSS并返回访问地址
     */

    //带姓名接口
    @Override
    public String uploadFile(MultipartFile file,String name) throws Exception {
        // 原始文件名
        String originalName = file.getOriginalFilename();

        // 后缀
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        // 唯一文件名，防止覆盖
        String fileName = name+"_"+UUID.randomUUID() + suffix;

        InputStream inputStream = file.getInputStream();
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream);
        ossClient.putObject(request);

        // 拼接外网访问链接，URL编码文件名防止中文导致图片加载失败
        String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20"); // 空格转%20
        return "https://" + bucketName + "." + endpoint + "/" + encodedName;
    }

    //不带姓名接口
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String originalName = file.getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;

        InputStream inputStream = file.getInputStream();
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream);
        ossClient.putObject(request);

        return "https://" + bucketName + "." + endpoint + "/" + fileName;
    }
}
