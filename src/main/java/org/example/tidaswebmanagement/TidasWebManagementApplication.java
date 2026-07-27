package org.example.tidaswebmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.tidaswebmanagement.mapper") //只新增这一行！
public class TidasWebManagementApplication {


    //管理员pengge  liuge666
    public static void main(String[] args) {
        SpringApplication.run(TidasWebManagementApplication.class, args);
    }

}