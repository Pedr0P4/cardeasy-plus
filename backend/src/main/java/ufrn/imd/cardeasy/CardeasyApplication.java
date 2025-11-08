package ufrn.imd.cardeasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ufrn.imd.cardeasy.configurations.AppSecurityProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppSecurityProperties.class)
public class CardeasyApplication {
	public static void main(String[] args) {
		SpringApplication.run(CardeasyApplication.class, args);
	};
};
