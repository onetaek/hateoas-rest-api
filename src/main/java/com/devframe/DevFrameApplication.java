package com.devframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevFrameApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevFrameApplication.class, args);
    }

}
