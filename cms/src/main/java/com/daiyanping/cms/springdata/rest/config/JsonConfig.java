package com.daiyanping.cms.springdata.rest.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.hal.Jackson2HalModule;

import java.util.TimeZone;

@Configuration
public class JsonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            // 开启缩进
            jacksonObjectMapperBuilder.indentOutput(true);
            jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        };
    }

    public Jackson2HalModule jackson2HalModule() {
        Jackson2HalModule jackson2HalModule = new Jackson2HalModule();
        return jackson2HalModule;
    }
}
