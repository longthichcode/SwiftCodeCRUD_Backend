package com.project.Security.DTO;

import java.util.List;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private List<String> roles ;
    private String username;
    
    public AuthResponse(String accessToken, String refreshToken, List<String> roles, String username) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.roles = roles;
		this.username = username;
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
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
    
	
    
}