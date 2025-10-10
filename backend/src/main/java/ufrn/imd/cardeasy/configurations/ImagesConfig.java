package ufrn.imd.cardeasy.configurations;

import java.awt.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
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

  @EventListener(ApplicationReadyEvent.class)
    public void initializeDirectories() {
      try {
        Path dirPath = Paths.get("database/files/avatars");
        Files.createDirectories(dirPath);
      } catch (Exception e) {
        e.printStackTrace();
      };
    };
};
