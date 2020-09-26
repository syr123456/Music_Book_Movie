package com.wsk.life.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.*;
/*
*
 * @DESCRIPTION :静态资源访问路径
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/1/1  13:28
 */

//@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${absoluteImgPath}")
    String absoluteImgPath;
    @Value("${sonImgPath}")
    String sonImgPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/image/");
        registry.addResourceHandler("/js/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/css/");
        registry.addResourceHandler(sonImgPath + "**").addResourceLocations("file:"+absoluteImgPath);
        //        registry.addResourceHandler("/images/**").addResourceLocations("file:/D:/image/");
//        registry.addResourceHandler("/musics/**").addResourceLocations("file:/G:/down/");
        //服务器,
//        已经使用NG，加载静态资源了
        //registry.addResourceHandler("/images/**").addResourceLocations("/Movie/images");
//         registry.addResourceHandler("/music/**").addResourceLocations("/home/wsk/down/");
        //WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}