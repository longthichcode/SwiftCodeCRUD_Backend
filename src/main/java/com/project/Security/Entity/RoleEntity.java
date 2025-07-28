package com.project.Security.Entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SC_ROLES")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sc_role_seq")
    @SequenceGenerator(name = "sc_role_seq", sequenceName = "SC_ROLE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Integer ID;

    @Column(name = "ROLE_NAME", unique = true, nullable = false, length = 50)
    private String ROLE_NAME;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "SC_ROLE_FUNCTION",
        joinColumns = @JoinColumn(name = "ROLE_ID"),
        inverseJoinColumns = @JoinColumn(name = "FUNCTION_ID")
    )
    private Set<FunctionEntity> functions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "SC_ROLE_PERMISSION",
        joinColumns = @JoinColumn(name = "ROLE_ID"),
        inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
    )
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "ROLES")
    private Set<UserEntity> USERS = new HashSet<>();

    // Constructors
    public RoleEntity() {}

    public RoleEntity(Integer ID, String ROLE_NAME) {
        this.ID = ID;
        this.ROLE_NAME = ROLE_NAME;
    }

    // Getters and Setters
    public Integer getID() { return ID; }
    public void setID(Integer ID) { this.ID = ID; }
    public String getROLE_NAME() { return ROLE_NAME; }
    public void setROLE_NAME(String ROLE_NAME) { this.ROLE_NAME = ROLE_NAME; }
    public Set<FunctionEntity> getFunctions() { return functions; }
    public void setFunctions(Set<FunctionEntity> functions) { this.functions = functions; }
    public Set<Permission> getPermissions() { return permissions; }
    public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }
    public Set<UserEntity> getUSERS() { return USERS; }
    public void setUSERS(Set<UserEntity> USERS) { this.USERS = USERS; }
}