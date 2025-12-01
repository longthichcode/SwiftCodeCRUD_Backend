package com.project.Security.Entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SC_USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sc_users_seq")
    @SequenceGenerator(name = "sc_users_seq", sequenceName = "SC_USERS_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Integer ID;

    @Column(name = "USER_NAME", unique = true, nullable = false, length = 50)
    private String USER_NAME;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String PASSWORD;

    @Column(name = "ENABLED", nullable = false)
    private Boolean ENABLED = true;

    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "SC_USER_ROLE",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<RoleEntity> ROLES = new HashSet<>();
    
    public UserEntity() {
    }

    
    public UserEntity(Integer ID, String USER_NAME, String PASSWORD, Boolean ENABLED) {
        this.ID = ID;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
        this.ENABLED = ENABLED;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public Boolean getENABLED() {
        return ENABLED;
    }

    public void setENABLED(Boolean ENABLED) {
        this.ENABLED = ENABLED;
    }

    public Set<RoleEntity> getROLES() {
        return ROLES;
    }

    public void setROLES(Set<RoleEntity> ROLES) {
        this.ROLES = ROLES;
    }


	@Override
	public String toString() {
		return "UserEntity [ID=" + ID + ", USER_NAME=" + USER_NAME + ", PASSWORD=" + PASSWORD + ", ENABLED=" + ENABLED
				+ ", ROLES=" + ROLES + "]";
	}
    
    
}