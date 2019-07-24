package com.mf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
public class JxcApplication {

	public static void main(String[] args) {
		SpringApplication.run(JxcApplication.class, args);
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return container -> {
			container.setSessionTimeout(60 * 60 * 24);/* 单位为S */
		};
	}

	/**
	 * 文件上传配置
	 * 
	 * @return
	 */
	/*@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 文件最大
		factory.setMaxFileSize("2048MB"); // KB,MB
		// / 设置总上传数据总大小
		factory.setMaxRequestSize("2048MB");
		return factory.createMultipartConfig();
	}*/

	@Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        //resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize(20971520);
        //上传文件大小 1024M 1024*1024*1024
        resolver.setMaxUploadSize(1024 * 1024 * 1024);
        return resolver;
    }
	
}