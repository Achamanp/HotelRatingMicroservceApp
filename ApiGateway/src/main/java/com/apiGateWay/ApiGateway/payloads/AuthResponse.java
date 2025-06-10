package com.apiGateWay.ApiGateway.payloads;

import java.util.Collection;

public class AuthResponse {
	private String userId;
	private String accessToken;
	private String refreshToken;
	private long expiresAt;
	private Collection<String> authorities;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public long getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}
	public Collection<String> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Collection<String> authorities) {
		this.authorities = authorities;
	}
	
	

}
