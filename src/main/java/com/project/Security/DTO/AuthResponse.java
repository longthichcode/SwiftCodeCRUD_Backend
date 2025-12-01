package com.project.Security.DTO;

import java.util.List;

public class AuthResponse {
    private String accessToken;
    private List<String> roles ;
    private List<String> functions;
    private List<String> permissions;
    private String username;
    
    public AuthResponse(String accessToken, List<String> roles, List<String> functions, List<String> permissions, String username) {
		this.accessToken = accessToken;
		this.roles = roles;
		this.functions = functions;
		this.permissions = permissions;
		this.username = username;
	}
    
    public AuthResponse() {
    	
    }

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getFunctions() {
		return functions;
	}

	public void setFunctions(List<String> functions) {
		this.functions = functions;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
    
}