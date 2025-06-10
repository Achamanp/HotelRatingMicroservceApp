package com.apiGateWay.ApiGateway.controllers;

import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiGateWay.ApiGateway.payloads.AuthResponse;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public Mono<AuthResponse> login(
            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,
            @AuthenticationPrincipal OidcUser user) {
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(user.getEmail());
        authResponse.setAccessToken(client.getAccessToken().getTokenValue());
        
        if (client.getRefreshToken() != null) {
            authResponse.setRefreshToken(client.getRefreshToken().getTokenValue());
        }
        
        authResponse.setExpiresAt(client.getAccessToken().getExpiresAt().getEpochSecond());
        authResponse.setAuthorities(user.getAuthorities().stream()
            .map(grantedAuthority -> grantedAuthority.getAuthority())
            .collect(Collectors.toList()));
        
        return Mono.just(authResponse);
    }
}