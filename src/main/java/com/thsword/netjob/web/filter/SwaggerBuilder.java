package com.thsword.netjob.web.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/*import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;*/

//@Configuration    // 配置注解，自动在本类上下文加载一些环境变量信息
//@EnableSwagger2   // 使swagger2生效
//@EnableWebMvc
//@ComponentScan(basePackages = {"com.custom.web"})  //需要扫描的包路径
public class SwaggerBuilder extends WebMvcConfigurationSupport {

   /* @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("business-api")
                .select()
                .paths(PathSelectors.regex(".*app.*"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private List<ApiKey> securitySchemes() {
    	List<ApiKey> apiKeys= new ArrayList<>();
    	apiKeys.add(new ApiKey("clientId", "客户端ID", "header"));
    	apiKeys.add(new ApiKey("clientSecret", "客户端秘钥", "header"));
    	apiKeys.add(new ApiKey("accessToken", "客户端访问标识", "header"));
    	return apiKeys;
    }

    private List<SecurityContext> securityContexts() {
    	List<SecurityContext> contents= new ArrayList<>();
    	contents.add( SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/*.*"))
                .build());
        return contents;
    }

    List<SecurityReference> defaultAuth() {
    	List<SecurityReference> refers= new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        refers.add(new SecurityReference("clientId", authorizationScopes));
        refers.add(new SecurityReference("clientSecret", authorizationScopes));
        refers.add(new SecurityReference("accessToken", authorizationScopes));
        return refers;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring 中使用Swagger2构建RESTful API")
                .termsOfServiceUrl("http://blog.csdn.net/yangshijin1988")
                .description("此API提供接口调用")
                .license("License Version 2.0")
                .licenseUrl("http://blog.csdn.net/yangshijin1988")
                .version("2.0").build();
    }*/
}
