// UserDetailDTO.java
package com.project.Security.DTO;

import java.util.List;

public class UserResponse {
	
    private Integer id;
    private String username;
    private String password;
    private Integer enabled;
    private List<String> roles;
    // Constructors
    public UserResponse() {}
    public UserResponse(Integer id, String username, String password, Integer enabled, List<String> roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
    
}