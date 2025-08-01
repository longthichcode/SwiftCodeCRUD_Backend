package com.project.Security.Controller;

import com.project.Security.DTO.AuthRequest;
import com.project.Security.DTO.FunctionResponse;
import com.project.Security.DTO.RoleResponse;
import com.project.Security.DTO.UserDetailDTO;
import com.project.Security.DTO.UserResponse;
import com.project.Security.Entity.FunctionEntity;
import com.project.Security.Entity.Permission;
import com.project.Security.Entity.RoleEntity;
import com.project.Security.Entity.UserEntity;
import com.project.Security.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	// ===== USER MANAGEMENT =====

	// Đăng ký người dùng mới
	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@RequestBody AuthRequest request) {
		try {
			UserEntity newUser = userService.createUser(request.getUsername(), request.getPassword(), "USER");
			Map<String, String> response = new HashMap<>();
			response.put("message", "Đăng kí tài khoản thành công : " + newUser.getUSER_NAME());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("message", "Error registering user: " + e.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}

	// Tạo người dùng (dành cho Super Admin)
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestParam String username, @RequestParam String password,
			@RequestParam String roleName) {
		try {
			UserEntity user = userService.createUser(username, password, roleName);
			return ResponseEntity.ok("User created: " + user.getUSER_NAME());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	// Cập nhật thông tin người dùng
	@PutMapping("/{id}/update")
	public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestParam(required = false) String username,
			@RequestParam(required = false) String password, @RequestParam(required = false) String roleName) {
		try {
			UserEntity user = userService.updateUser(id, username, password, roleName);
			return ResponseEntity.ok("User updated: " + user.getUSER_NAME());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	// Chỉnh sửa vai trò người dùng
	@PutMapping("/{userId}/update-roles")
	public ResponseEntity<?> updateUserRoles(@PathVariable Integer userId, @RequestBody List<String> roleNames) {
		try {
			List<String> cleanedRoleNames = roleNames.stream().map(role -> role.replace("ROLE_", ""))
					.collect(Collectors.toList());

			userService.updateUserRoles(userId, cleanedRoleNames);
			Map<String, String> response = new HashMap<>();
			response.put("message", "User roles updated successfully");
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return ResponseEntity.status(400).body(errorResponse);
		}
	}

	// Cập nhật trạng thái người dùng
	@PutMapping("/{userId}/update-status")
	public ResponseEntity<?> updateUserStatus(@PathVariable Integer userId, @RequestBody int status) {
		try {
			userService.updateUserStatus(userId, status);
			Map<String, String> response = new HashMap<>();
			response.put("message", "User status updated successfully");
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return ResponseEntity.status(400).body(errorResponse);
		}
	}

	// Lấy vai trò của người dùng
	@GetMapping("/{userId}/roles")
	public ResponseEntity<List<String>> getUserRoles(@PathVariable Integer userId) {
		try {
			List<String> roleNames = userService.getUserRoles(userId);
			return ResponseEntity.ok(roleNames);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(404).body(Collections.emptyList());
		}
	}

	// Lấy tất cả người dùng
	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserDetailDTO> users = userService.getAllUsers();
		List<UserResponse> userResponses = new ArrayList<>();

		for (UserDetailDTO user : users) {
			List<String> roleNames = userService.findRolesByUserId(user.getId());

			UserResponse userResponse = new UserResponse();
			userResponse.setId(user.getId());
			userResponse.setUsername(user.getUsername());
			userResponse.setPassword(user.getPassword());
			userResponse.setEnabled(user.getEnabled());
			userResponse.setRoles(roleNames);

			userResponses.add(userResponse);
		}

		return ResponseEntity.ok(userResponses);
	}

	// ===== ROLE MANAGEMENT =====
	// Lấy tất cả vai trò
	@GetMapping("/roles")
	public ResponseEntity<?> getRolesAndFunctions(@RequestParam(required = false) Integer roleId) {
	    try {
	        List<Map<String, Object>> roleResponses;

	        // Nếu roleId được cung cấp, lấy thông tin cho vai trò cụ thể
	        if (roleId != null) {
	            // Kiểm tra vai trò tồn tại
	            RoleEntity role = userService.getAllRoleEntities().stream()
	                    .filter(r -> r.getID().equals(roleId))
	                    .findFirst()
	                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

	            Map<String, Object> roleMap = new HashMap<>();
	            roleMap.put("id", role.getID());
	            roleMap.put("name", role.getROLE_NAME());

	            // Lấy danh sách function của vai trò
	            List<Map<String, Object>> functionList = new ArrayList<>();
	            List<FunctionEntity> functions = userService.getFunctionByRoleId(roleId);

	            for (FunctionEntity function : functions) {
	                Map<String, Object> functionMap = new HashMap<>();
	                functionMap.put("id", function.getId());
	                functionMap.put("name", function.getName());
	                functionMap.put("description", function.getDescription());

	                // Lấy danh sách permission trực tiếp của role từ SC_ROLE_PERMISSION
	                List<Permission> permissions = userService.getPermissionsByRoleId(roleId); // Sửa lại để dùng đúng phương thức
	                List<Map<String, Object>> permissionList = new ArrayList<>();

	                for (Permission permission : permissions) {
	                    Map<String, Object> permissionMap = new HashMap<>();
	                    permissionMap.put("id", permission.getId());
	                    permissionMap.put("name", permission.getName());
	                    permissionMap.put("description", permission.getDescription());
	                    permissionMap.put("url", permission.getUrl());
	                    permissionMap.put("method", permission.getMethod());
	                    permissionList.add(permissionMap);
	                }

	                functionMap.put("permissions", permissionList);
	                functionList.add(functionMap);
	            }

	            roleMap.put("functions", functionList);
	            roleResponses = Collections.singletonList(roleMap);
	        } else {
	            // Nếu không có roleId, lấy tất cả vai trò
	            List<RoleEntity> roles = userService.getAllRoleEntities();
	            roleResponses = new ArrayList<>();

	            for (RoleEntity role : roles) {
	                Map<String, Object> roleMap = new HashMap<>();
	                roleMap.put("id", role.getID());
	                roleMap.put("name", role.getROLE_NAME());

	                // Lấy danh sách userNames
	                List<String> userNames = userService.findUsersByRoleId(role.getID());

	                // Lấy danh sách function của vai trò
	                List<Map<String, Object>> functionList = new ArrayList<>();
	                List<FunctionEntity> functions = userService.getFunctionByRoleId(role.getID());

	                for (FunctionEntity function : functions) {
	                    Map<String, Object> functionMap = new HashMap<>();
	                    functionMap.put("id", function.getId());
	                    functionMap.put("name", function.getName());
	                    functionMap.put("description", function.getDescription());

	                    // Lấy danh sách permission trực tiếp của role từ SC_ROLE_PERMISSION
	                    List<Permission> permissions = userService.getPermissionsByRoleId(role.getID()); // Sửa lại để dùng đúng phương thức
	                    List<Map<String, Object>> permissionList = new ArrayList<>();

	                    for (Permission permission : permissions) {
	                        Map<String, Object> permissionMap = new HashMap<>();
	                        permissionMap.put("id", permission.getId());
	                        permissionMap.put("name", permission.getName());
	                        permissionMap.put("description", permission.getDescription());
	                        permissionMap.put("url", permission.getUrl());
	                        permissionMap.put("method", permission.getMethod());
	                        permissionList.add(permissionMap);
	                    }

	                    functionMap.put("permissions", permissionList);
	                    functionList.add(functionMap);
	                }

	                roleMap.put("functions", functionList);
	                roleMap.put("users", userNames); // Thêm danh sách userNames
	                roleResponses.add(roleMap);
	            }
	        }

	        return ResponseEntity.ok(roleResponses);
	    } catch (IllegalArgumentException e) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("error", e.getMessage());
	        return ResponseEntity.status(404).body(errorResponse);
	    } catch (Exception e) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Lỗi khi lấy thông tin vai trò: " + e.getMessage());
	        return ResponseEntity.status(500).body(errorResponse);
	    }
	}

	// sửa quyền của vai trò
	@PutMapping("/roles/{roleId}/permissions")
	public ResponseEntity<?> editPermissionsByRoleId(@PathVariable Integer roleId,
			@RequestBody List<String> permissionNames) {
		try {
			userService.editPermissionsByRoleId(roleId, permissionNames);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Permissions updated successfully for role ID: " + roleId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Error updating permissions: " + e.getMessage());
			return ResponseEntity.status(400).body(errorResponse);
		}
	}

	// Lấy tất cả chức năng theo vai trò
	@GetMapping("/roles/{roleId}/functions")
	public ResponseEntity<List<String>> getFunctionsByRoleId(@PathVariable Integer roleId) {
		try {
			List<String> functionNames = userService.getFunctionByRoleId(roleId).stream().map(FunctionEntity::getName)
					.collect(Collectors.toList());
			return ResponseEntity.ok(functionNames);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Collections.emptyList());
		}
	}

	// Sửa chức năng của vai trò
	@PutMapping("/roles/{roleId}/functions")
	public ResponseEntity<?> editFunctionsByRoleId(@PathVariable Integer roleId,
			@RequestBody List<String> functionNames) {
		try {
			userService.editFunctionsByRoleId(roleId, functionNames);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Functions updated successfully for role ID: " + roleId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Error updating functions: " + e.getMessage());
			return ResponseEntity.status(400).body(errorResponse);
		}
	}

	// ===== FUNCTION & PERMISSION =====

	// Lấy tất cả chức năng
	@GetMapping("/functions")
	public ResponseEntity<List<FunctionResponse>> getAllFunctions() {
		List<FunctionEntity> functions = userService.getAllFunctions();
		List<FunctionResponse> functionResponses = new ArrayList<>();

		for (FunctionEntity function : functions) {
			List<String> permissionsNames = userService.getPermissionsByFunctionId(function.getId()).stream()
					.map(Permission::getName).collect(Collectors.toList());
			List<String> permissionsDes = userService.getPermissionsByFunctionId(function.getId()).stream()
					.map(Permission::getDescription).collect(Collectors.toList());
			List<String> methods = userService.getPermissionsByFunctionId(function.getId()).stream()
					.map(Permission::getMethod).collect(Collectors.toList());
			List<String> urls = userService.getPermissionsByFunctionId(function.getId()).stream()
					.map(Permission::getUrl).collect(Collectors.toList());

			FunctionResponse response = new FunctionResponse(function.getId(), function.getName(),
					function.getDescription(), permissionsNames, permissionsDes, methods, urls);
			functionResponses.add(response);
		}
		return ResponseEntity.ok(functionResponses);
	}

	// Lấy quyền theo chức năng
	@GetMapping("/functions/{functionId}/permissions")
	public ResponseEntity<List<Permission>> getPermissionsByFunctionId(@PathVariable Integer functionId) {
		try {
			List<Permission> permissions = userService.getPermissionsByFunctionId(functionId);
			return ResponseEntity.ok(permissions);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Collections.emptyList());
		}
	}

	// Lấy tất cả quyền
	@GetMapping("/permissions")
	public ResponseEntity<List<String>> getAllPermissions() {
		try {
			List<Permission> permissions = userService.getAllPermissions();
			List<String> permissionNames = permissions.stream().map(Permission::getName).collect(Collectors.toList());
			return ResponseEntity.ok(permissionNames);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Collections.emptyList());
		}
	}
}