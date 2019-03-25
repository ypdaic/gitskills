package com.daiyanping.cms;

import lombok.Data;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Data
@Component
//实现ResourceLoader接口，用于获取spring容器的ResourceLoader
public class ResourceLoaderAwareTest implements ResourceLoaderAware {

	private ResourceLoader resourceLoader;

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
