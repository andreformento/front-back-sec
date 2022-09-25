package com.andreformento.app.config
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class ResourceConfiguration : WebFluxConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.also {
            it
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/frontend/build/static/")
            it
                .addResourceHandler("/*.js")
                .addResourceLocations("classpath:/frontend/build/")
            it
                .addResourceHandler("/*.json")
                .addResourceLocations("classpath:/frontend/build/")
            it
                .addResourceHandler("/*.ico")
                .addResourceLocations("classpath:/frontend/build/")
            it
                .addResourceHandler("/*.png")
                .addResourceLocations("classpath:/frontend/build/")
            it
                .addResourceHandler("/index.html")
                .addResourceLocations("classpath:/frontend/build/index.html")
        }
    }

}
