package com.fx.gateway.infrastructure.web;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class PageableTestMvcConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
		resolvers.add(sortResolver);
		resolvers.add(new PageableHandlerMethodArgumentResolver(sortResolver));
	}
}
