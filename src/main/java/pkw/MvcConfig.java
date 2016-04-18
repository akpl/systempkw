package pkw;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/css/**")
                .addResourceLocations("/css/");
        registry
                .addResourceHandler("/fonts/**")
                .addResourceLocations("/fonts/");
        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("/js/");
        registry
                .addResourceHandler("/img/**")
                .addResourceLocations("/img/");
        registry
                .addResourceHandler("/img2/**")
                .addResourceLocations("/img2/");
        registry
                .addResourceHandler("/template/**")
                .addResourceLocations("/template/");
    }
}