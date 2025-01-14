
package com.purplebits.emrd2.swagger;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author prayas
 * @description Swagger
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends WebMvcConfigurerAdapter {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	private ApiKey apiKeys() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private List<SecurityContext> securityContexts() {
		return Arrays.asList(SecurityContext.builder().securityReferences(secRefs()).build());
	}

	private List<SecurityReference> secRefs() {
		AuthorizationScope scopes = new AuthorizationScope("global", "accessEverything");
		return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[] { scopes }));
	}

	@Bean
	@Conditional(SwaggerCondition.class)
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getAppInfo()).securityContexts(securityContexts())
				.securitySchemes(Arrays.asList(apiKeys())).groupName("EPRO api-v1.0").select()
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();

	}

	/**
	 * @return
	 */
	private ApiInfo getAppInfo() {
		return new ApiInfo("EPRO", " API Documentation ", "Version 1", "https://www.purpledocs.com/terms-of-service/",
				"PurpleDocs", "Non Open Source", "https://www.purpledocs.com/privacy-policy/");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// enabling swagger-ui part for visual documentation
		super.addResourceHandlers(registry);
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
