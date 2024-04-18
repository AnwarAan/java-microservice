package microservice.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class WebClientConfig {
    @Bean
    public WebClient.Builder webBuilder() {
        return WebClient.builder();
    }
}
