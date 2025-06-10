package com.FirstMicroServicesApplication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestTemplate;

import com.FirstMicroServicesApplication.config.RestTemplateInterceptor;

@SpringBootApplication
@EnableFeignClients
public class FirstMicroServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstMicroServicesApplication.class, args);
        System.out.println("\n Server is Running on port 8080");
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(OAuth2AuthorizedClientManager clientManager) {
        RestTemplate restTemplate = new RestTemplate();

        // Add OAuth2 interceptor to RestTemplate
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new RestTemplateInterceptor(clientManager)); // Fixed: clientManager instead of manag

        restTemplate.setInterceptors(interceptors); // Fixed: set the interceptors list instead of null

        return restTemplate;
    }

    @Bean
    public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        // Configure the authorized client provider
        OAuth2AuthorizedClientProvider authorizedClientProvider =
            OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .clientCredentials()
                .build();

        // Create the authorized client manager
        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
            new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientRepository);

        // Set the authorized client provider
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
    
}