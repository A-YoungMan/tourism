package com.oaec.tourism.configurationutil;

import com.oaec.tourism.util.ContextPath;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/*相对路径类*/
@Configuration
public class MyWebImgConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("执行文件上传流!");
        registry.addResourceHandler("/upload/Tourism/**").addResourceLocations("file:"+ ContextPath.ABC_PATH);
    }
}
