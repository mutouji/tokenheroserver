package org.delphy.tokenheroserver.configuration;

import org.delphy.tokenheroserver.aop.RequestLimitContract;
import org.delphy.tokenheroserver.handler.ArgumentResolver;
import org.delphy.tokenheroserver.handler.TokenInterceptor;
import org.delphy.tokenheroserver.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author mutouji
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    private IndexService indexService;

    public WebConfig(@Autowired IndexService indexService) {
        this.indexService = indexService;
    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLimitContract()).excludePathPatterns(
                "/login", "/verifycode","/error", "/**.html","/v2/api-docs","/swagger-resources/**","/webjars/**");
        registry.addInterceptor(new TokenInterceptor(indexService)).excludePathPatterns(
                "/login", "/verifycode","/error", "/**.html","/v2/api-docs","/swagger-resources/**","/webjars/**");
        super.addInterceptors(registry);
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ArgumentResolver());
    }
}
