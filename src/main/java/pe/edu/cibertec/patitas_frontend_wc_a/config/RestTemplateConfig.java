package pe.edu.cibertec.patitas_frontend_wc_a.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
      .rootUri("http://localhost:8081/autenticacion")
      .setReadTimeout(Duration.ofSeconds(90))
      .build();
  }


}
