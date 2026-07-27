package org.example.tidaswebmanagement.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    @ConditionalOnWebApplication  // 仅在真实 Servlet 容器（Tomcat）中创建，测试环境自动跳过
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}