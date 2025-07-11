package com.project.Security.DTO;

import java.util.List;

public class RoleResponse {
	private int roleId;
	private String roleName;
	private List<String> userNames;
	private List<String> functionsNames;
	private List<String> functionsDes;
	public RoleResponse(int roleId, String roleName, List<String> userNames, List<String> functionsNames, List<String> functionsDes) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.userNames = userNames;
		this.functionsNames = functionsNames;
		this.functionsDes = functionsDes;
	}
	public int getRoleId() {
		return roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<String> getUserNames() {
		return userNames;
	}
	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}
	public List<String> getFunctionsNames() {
		return functionsNames;
	}
	public void setFunctionsNames(List<String> functionsNames) {
		this.functionsNames = functionsNames;
	}
	public List<String> getFunctionsDes() {
		return functionsDes;
	}
	public void setFunctionsDes(List<String> functionsDes) {
		this.functionsDes = functionsDes;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
