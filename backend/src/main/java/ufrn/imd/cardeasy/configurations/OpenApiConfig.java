package ufrn.imd.cardeasy.configurations;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@Configuration
@OpenAPIDefinition(
  info = @Info(title = "Cardeasy Plus", version = "v1"),
  security = @SecurityRequirement(name = "token"),
  tags = {
    @Tag(name = "Authentication"),
    @Tag(name = "Accounts"),
    @Tag(name = "Teams"),
    @Tag(name = "Participations"),
    @Tag(name = "Projects"),
    @Tag(name = "Budgets"),
    @Tag(name = "Stages"),
    @Tag(name = "CardLists"),
    @Tag(name = "Cards"),
    @Tag(name = "Tags"),
    @Tag(name = "Assignments"),
  }
)
@SecurityScheme(
  name = "token",
  type = SecuritySchemeType.HTTP,
  scheme = "bearer",
  bearerFormat = "JWT"
) 
public class OpenApiConfig {};
