package jp.co.rakus.config;


import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Thymeleafを使用するための設定.
 * 
 * @author kento.uemura
 *
 */
@Configuration
public class ThymeleafConfig {

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver(ServletContext servletContext) {
		ServletContextTemplateResolver servletContextTemplateResolver = new ServletContextTemplateResolver(servletContext);
		servletContextTemplateResolver.setPrefix("/WEB-INF/templates");
		servletContextTemplateResolver.setSuffix(".html");
		servletContextTemplateResolver.setTemplateMode("HTML5");
		servletContextTemplateResolver.setCharacterEncoding("UTF-8");
		servletContextTemplateResolver.setCacheable(true);
		
		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		springTemplateEngine.addDialect(new SpringSecurityDialect());
		springTemplateEngine.setTemplateResolver(servletContextTemplateResolver);
		
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setCharacterEncoding("utf-8");
		thymeleafViewResolver.setTemplateEngine(springTemplateEngine);
		
		return thymeleafViewResolver;
	}
}
