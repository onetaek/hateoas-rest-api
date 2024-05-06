package com.devframe.global.config;

import com.devframe.global.common.argumentresolver.CustomValidator;
import com.devframe.global.common.argumentresolver.ListValidationArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final CustomValidator customValidator;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ListValidationArgumentResolver(customValidator));
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder, HttpMethodSerializer httpMethodSerializer) {
        ObjectMapper objectMapper = builder.build();
        SimpleModule module = new SimpleModule();
        module.addSerializer(HttpMethod.class, httpMethodSerializer);
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
