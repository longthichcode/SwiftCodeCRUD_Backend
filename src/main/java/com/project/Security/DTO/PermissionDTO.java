package com.project.Security.DTO;

public class PermissionDTO {
    private Long id;
    private String name;
    private String description;
    private String url;
    private String method;

    public PermissionDTO(Long id, String name, String description, String url, String method) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.method = method;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}