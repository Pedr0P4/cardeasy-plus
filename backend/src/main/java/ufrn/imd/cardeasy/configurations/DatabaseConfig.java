package ufrn.imd.cardeasy.configurations;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {
  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.driverClassName}")
  private String driver;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(this.url);
    config.setDriverClassName(this.driver);
    config.setUsername(this.username);
    config.setPassword(this.password);
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setPoolName("h2-pool");
    return new HikariDataSource(config);
  };
};
