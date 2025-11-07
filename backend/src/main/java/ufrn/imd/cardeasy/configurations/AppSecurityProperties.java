package ufrn.imd.cardeasy.configurations;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.security")
public class AppSecurityProperties {
  private Cors cors;
  private Jwt jwt;

  @Getter
  @Setter
  public static class Cors {
    private Allowed allowed;

    @Getter
    @Setter
    public static class Allowed {
      private List<String> origins;
      private List<String> methods;
      private List<String> headers;
    };
  };

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Jwt {
    private String secret;
  };
};
