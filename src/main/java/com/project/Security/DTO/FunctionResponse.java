package com.project.Security.DTO;

import java.util.List;

public class FunctionResponse {
	private int functionId;
	private String functionName;
	private String functionDescription;	
	private List<String> permissionsNames;
	private List<String> permissionsDes;
	private List<String> methods;
	private List<String> urls;
	public FunctionResponse(int functionId, String functionName, String functionDescription,
			List<String> permissionsNames, List<String> permissionsDes, List<String> methods, List<String> urls) {
		this.functionId = functionId;
		this.functionName = functionName;
		this.functionDescription = functionDescription;
		this.permissionsNames = permissionsNames;
		this.permissionsDes = permissionsDes;
		this.methods = methods;
		this.urls = urls;
	}
	public int getFunctionId() {
		return functionId;
	}
	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getFunctionDescription() {
		return functionDescription;
	}
	public void setFunctionDescription(String functionDescription) {
		this.functionDescription = functionDescription;
	}
	public List<String> getPermissionsNames() {
		return permissionsNames;
	}
	public void setPermissionsNames(List<String> permissionsNames) {
		this.permissionsNames = permissionsNames;
	}
	public List<String> getPermissionsDes() {
		return permissionsDes;
	}
	public void setPermissionsDes(List<String> permissionsDes) {
		this.permissionsDes = permissionsDes;
	}
	public List<String> getMethods() {
		return methods;
	}
	public void setMethods(List<String> methods) {
		this.methods = methods;
	}
	public List<String> getUrls() {
		return urls;
	}
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	
	
}
