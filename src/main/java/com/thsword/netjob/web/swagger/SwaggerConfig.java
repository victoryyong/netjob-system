package com.thsword.netjob.web.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
// 让Spring来加载该类配置
@EnableSwagger2
// 启用Swagger2
@EnableWebMvc
public class SwaggerConfig {
	@Bean
	public Docket createAppApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(appInfo())
				.select()
				
				.apis(RequestHandlerSelectors
						.basePackage("com.thsword.netjob.web.controller.app")) 
				.paths(PathSelectors.any()).build().groupName("APP");
	}
	
	@Bean
	public Docket createAdminApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(adminInfo())
				.select()
				.apis(RequestHandlerSelectors
						.basePackage("com.thsword.netjob.web.controller.admin")) 
				.paths(PathSelectors.any()).build().groupName("ADMIN");
	}

	private ApiInfo appInfo() {
		return new ApiInfoBuilder().title("网约移动端接口列表 v1.1.0") 
				.termsOfServiceUrl("http://wy-168.com/netjob/swagger-ui.html") 
				.version("1.1.0").build();
	}
	
	private ApiInfo adminInfo() {
		return new ApiInfoBuilder().title("网约管理端接口列表 v1.1.0") 
				.termsOfServiceUrl("http://wy-168.com/netjob/swagger-ui.html")
				.version("1.1.0").build();
	}
}
