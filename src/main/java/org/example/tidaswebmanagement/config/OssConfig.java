package org.example.tidaswebmanagement.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;
    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Bean(destroyMethod = "shutdown")
    public OSS ossClient() {
        ClientBuilderConfiguration config = new ClientBuilderConfiguration();
        config.setConnectionTimeout(10000);       // 连接超时 10 秒
        config.setSocketTimeout(30000);           // 读写超时 30 秒
        config.setMaxErrorRetry(1);               // 失败最多重试 1 次
        config.setSupportCname(true);             // 支持 CNAME 加速
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, config);
    }
}