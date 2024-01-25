package com.devframe.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
public class WebMvcConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder, HttpMethodSerializer httpMethodSerializer) {
        ObjectMapper objectMapper = builder.build();
        SimpleModule module = new SimpleModule();
        module.addSerializer(HttpMethod.class, httpMethodSerializer);
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
