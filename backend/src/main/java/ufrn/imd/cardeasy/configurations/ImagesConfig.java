package ufrn.imd.cardeasy.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ImagesConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(
    @NonNull ResourceHandlerRegistry registry
  ) {
    registry.addResourceHandler("/avatars/**")
      .addResourceLocations("file:database/files/avatars/")
      .addResourceLocations("file:../database/files/avatars/");
  };
};
