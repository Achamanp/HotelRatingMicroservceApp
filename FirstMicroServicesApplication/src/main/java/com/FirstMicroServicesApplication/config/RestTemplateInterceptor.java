package com.FirstMicroServicesApplication.config;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2AuthorizedClientManager clientManager;

    public RestTemplateInterceptor(OAuth2AuthorizedClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, 
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("okta")  // Your client registration ID
                .principal(authentication)
                .build();

            OAuth2AuthorizedClient authorizedClient = clientManager.authorize(authorizeRequest);
            
            if (authorizedClient != null) {
                String token = authorizedClient.getAccessToken().getTokenValue();
                request.getHeaders().add("Authorization", "Bearer " + token);
            }
        }

        return execution.execute(request, body);
    }
}
