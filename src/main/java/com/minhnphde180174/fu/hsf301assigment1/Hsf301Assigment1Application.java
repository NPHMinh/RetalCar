package com.minhnphde180174.fu.hsf301assigment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
  // Bao luon @Configuration
// va @EnableAutoConfiguration tu new, tu cau hinh: JPA/Hibernate, Tomcat, mvc
// va @ComponentScan quets ngay nhung class trong package
// ma co chua @Component va @Repository va @Service, @Controller , @RestController
public class Hsf301Assigment1Application {
    private static final Logger logger = LoggerFactory.getLogger(Hsf301Assigment1Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Hsf301Assigment1Application.class, args);

        try {
            new ProcessBuilder("cmd", "/c", "start", "http://localhost:9999")
                    .inheritIO()
                    .start();
        } catch (Exception e ){
            logger.error("Failed to open browser", e);
        }
    }
}
