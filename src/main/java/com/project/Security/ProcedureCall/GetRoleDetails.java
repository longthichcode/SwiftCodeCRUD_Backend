package com.project.Security.ProcedureCall;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;

@Repository
public class GetRoleDetails {
	
	private DataSource dataSource;

	public GetRoleDetails(DataSource dataSource) {
	    this.dataSource = dataSource;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAllRoleDetails() throws SQLException {
	    List<Map<String, Object>> results = new ArrayList<>();

	    try (Connection conn = dataSource.getConnection();
	         CallableStatement cs = conn.prepareCall("{call P_SC_GetAllRoleDetails(?, ?, ?, ?)}")) {

	        cs.registerOutParameter(1, OracleTypes.CURSOR);
	        cs.registerOutParameter(2, OracleTypes.CURSOR);
	        cs.registerOutParameter(3, OracleTypes.CURSOR);
	        cs.registerOutParameter(4, OracleTypes.CURSOR);

	        cs.execute();

	        // Load roles
	        Map<Integer, Map<String, Object>> roleMap = new HashMap<>();
	        try (ResultSet rs = (ResultSet) cs.getObject(1)) {
	            while (rs.next()) {
	                Map<String, Object> role = new HashMap<>();
	                role.put("id", rs.getInt("ID"));
	                role.put("name", rs.getString("ROLE_NAME"));
	                role.put("functions", new ArrayList<Map<String,Object>>());
	                role.put("permissions", new ArrayList<Map<String,Object>>());
	                role.put("users", new ArrayList<String>());
	                roleMap.put(rs.getInt("ID"), role);
	            }
	        }

	        // Functions
	        try (ResultSet rs = (ResultSet) cs.getObject(2)) {
	            while (rs.next()) {
	                Map<String, Object> f = new HashMap<>();
	                f.put("id", rs.getInt("ID"));
	                f.put("name", rs.getString("NAME"));
	                f.put("description", rs.getString("DESCRIPTION"));
	                ((List<Map<String,Object>>) roleMap.get(rs.getInt("ROLE_ID")).get("functions")).add(f);
	            }
	        }

	        // Permissions
	        try (ResultSet rs = (ResultSet) cs.getObject(3)) {
	            while (rs.next()) {
	                Map<String, Object> p = new HashMap<>();
	                p.put("id", rs.getInt("ID"));
	                p.put("name", rs.getString("NAME"));
	                p.put("description", rs.getString("DESCRIPTION"));
	                p.put("url", rs.getString("URL"));
	                p.put("method", rs.getString("METHOD"));
	                ((List<Map<String,Object>>) roleMap.get(rs.getInt("ROLE_ID")).get("permissions")).add(p);
	            }
	        }

	        // Users
	        try (ResultSet rs = (ResultSet) cs.getObject(4)) {
	            while (rs.next()) {
	                ((List<String>) roleMap.get(rs.getInt("ROLE_ID")).get("users"))
	                        .add(rs.getString("USER_NAME"));
	            }
	        }

	        results.addAll(roleMap.values());
	    }

	    return results;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getRoleDetailsById(int roleId) throws SQLException {
	    Map<String, Object> role = new HashMap<>();

	    try (Connection conn = dataSource.getConnection();
	         CallableStatement cs = conn.prepareCall("{call P_SC_GetRoleDetails(?, ?, ?, ?, ?)}")) {

	        cs.setInt(1, roleId);
	        cs.registerOutParameter(2, OracleTypes.CURSOR);
	        cs.registerOutParameter(3, OracleTypes.CURSOR);
	        cs.registerOutParameter(4, OracleTypes.CURSOR);
	        cs.registerOutParameter(5, OracleTypes.CURSOR);

	        cs.execute();

	        // Role
	        try (ResultSet rs = (ResultSet) cs.getObject(2)) {
	            if (rs.next()) {
	                role.put("id", rs.getInt("ID"));
	                role.put("name", rs.getString("ROLE_NAME"));
	                role.put("functions", new ArrayList<Map<String, Object>>());
	                role.put("permissions", new ArrayList<Map<String, Object>>());
	                role.put("users", new ArrayList<String>());
	            }
	        }

	        // Functions
	        try (ResultSet rs = (ResultSet) cs.getObject(3)) {
	            while (rs.next()) {
	                Map<String, Object> f = new HashMap<>();
	                f.put("id", rs.getInt("ID"));
	                f.put("name", rs.getString("NAME"));
	                f.put("description", rs.getString("DESCRIPTION"));
	                ((List<Map<String,Object>>) role.get("functions")).add(f);
	            }
	        }

	        // Permissions
	        try (ResultSet rs = (ResultSet) cs.getObject(4)) {
	            while (rs.next()) {
	                Map<String, Object> p = new HashMap<>();
	                p.put("id", rs.getInt("ID"));
	                p.put("name", rs.getString("NAME"));
	                p.put("description", rs.getString("DESCRIPTION"));
	                p.put("url", rs.getString("URL"));
	                p.put("method", rs.getString("METHOD"));
	                ((List<Map<String,Object>>) role.get("permissions")).add(p);
	            }
	        }

	        // Users
	        try (ResultSet rs = (ResultSet) cs.getObject(5)) {
	            while (rs.next()) {
	                ((List<String>) role.get("users")).add(rs.getString("USER_NAME"));
	            }
	        }
	    }

	    return role;
	}


}
