// UserDetailDTO.java
package com.project.Security.DTO;

public class UserDetailDTO {
    private Integer id;
    private String username;
    private String password;
    private Integer enabled;
    // Constructors
    public UserDetailDTO() {}

    public UserDetailDTO(Integer id, String username, String password, Integer enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    // Getters and Setters
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
}