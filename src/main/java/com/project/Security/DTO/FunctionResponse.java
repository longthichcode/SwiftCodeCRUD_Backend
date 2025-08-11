package com.project.Security.DTO;

import java.util.List;

public class FunctionResponse {
    private Integer id;
    private String name;
    private String description;
    private List<PermissionDTO> permissions;

    public FunctionResponse(Integer id, String name, String description, List<PermissionDTO> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<PermissionDTO> getPermissions() { return permissions; }
    public void setPermissions(List<PermissionDTO> permissions) { this.permissions = permissions; }
}