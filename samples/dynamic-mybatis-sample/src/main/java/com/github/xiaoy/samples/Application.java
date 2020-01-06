package com.github.xiaoy.samples;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.xiaoy.samples.mapper")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}