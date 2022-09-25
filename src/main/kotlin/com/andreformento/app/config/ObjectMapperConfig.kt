package com.andreformento.app.config

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


@Configuration
class ObjectMapperConfig {
    @Bean
    fun jackson2ObjectMapperBuilder(): Jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder()
        .serializationInclusion(JsonInclude.Include.NON_NULL)
        .failOnEmptyBeans(false)
        .failOnUnknownProperties(false)
        .simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
}
