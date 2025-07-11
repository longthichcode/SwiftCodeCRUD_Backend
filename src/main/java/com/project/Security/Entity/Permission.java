package com.project.Security.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "SC_PERMISSIONS")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sc_permission_seq")
    @SequenceGenerator(name = "sc_permission_seq", sequenceName = "SC_PERMISSION_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "URL", unique = false, nullable = false, length = 255)
    private String url;

    @Column(name = "METHOD", nullable = true, length = 10)
    private String method;
    
    // Constructors
    public Permission() {}
    
    public Permission(String name, String description, String url, String method) {
		this.name = name;
		this.description = description;
		this.url = url;
		this.method = method;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
    
}