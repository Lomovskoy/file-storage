package ru.animal.shelter.manager.filestorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.animal.shelter.manager.filestorage.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo getApiInfo() {
        return new  ApiInfo(
                "file-storage API",
                "Приложение файлового хранилища",
                "1.0.0",
                "http://javacv.h1n.ru/",
                new Contact(
                        "Lomovskoy Kirill",
                        "http://javacv.h1n.ru/",
                        "lomovskoy.kirill@yandex.ru"),
                "GNU",
                "http://javacv.h1n.ru/",
                new ArrayList<>());
    }

}
