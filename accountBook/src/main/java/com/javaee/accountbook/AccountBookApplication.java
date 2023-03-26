package com.javaee.accountbook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@MapperScan("com.javaee.accountbook.mapper")
public class AccountBookApplication {
    public static void main(String[] args)  {
        try{
            System.setProperty("java.awt.headless", "false");
            SpringApplicationBuilder builder = new SpringApplicationBuilder(AccountBookApplication.class);
            builder.headless(false).web(WebApplicationType.NONE).run(args);
        }catch (Exception e){

        }

//        SpringApplication.run(AccountBookApplication.class, args);

    }

}
