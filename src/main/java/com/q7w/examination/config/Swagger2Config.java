package com.q7w.examination.config;

import springfox.documentation.service.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
//注解开启 swagger2 功能
@EnableSwagger2
public class Swagger2Config {

    //是否开启swagger，正式环境一般是需要关闭的
    @Value("${swagger.enabled}")
    private boolean enableSwagger;

    @Bean
    public Docket createRestApi() {
        Parameter token = new ParameterBuilder().name("token")  //全局参数
                .description("用户登陆令牌")
                .parameterType("header")
                .modelRef(new ModelRef("String"))
                .required(true)
                .build();
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(token);
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameters)
                .apiInfo(apiInfo())
                //是否开启 (true 开启  false隐藏。生产环境建议隐藏)
                .enable(enableSwagger)
                .select()
                //扫描的路径包,设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.q7w.examination.controller"))
                //指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket LampState() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .groupName("获取Token")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.csdn.swaggershirojwtredis.controller"))
                .paths(PathSelectors.ant("/GetToken"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档标题(API名称)
                .title("河马在线考试后端口文档-Swagger")
                //文档描述
                .description("接口说明")
                //服务条款URL
                .termsOfServiceUrl("http://127.0.0.1:8081/")
                //联系信息
                .contact("JusticeGu")
                //版本号
                .version("1.0.0")
                .build();
    }

}

