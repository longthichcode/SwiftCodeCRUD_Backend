package com.project.Security.Entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SC_FUNCTIONS")
public class FunctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sc_function_seq")
    @SequenceGenerator(name = "sc_function_seq", sequenceName = "SC_FUNCTION_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "SC_FUNCTION_PERMISSION",
        joinColumns = @JoinColumn(name = "FUNCTION_ID"),
        inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
    )
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "functions")
    private Set<RoleEntity> roles = new HashSet<>();

    // Constructors
    public FunctionEntity() {}

    public FunctionEntity(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Permission> getPermissions() { return permissions; }
    public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }
    public Set<RoleEntity> getRoles() { return roles; }
    public void setRoles(Set<RoleEntity> roles) { this.roles = roles; }
}