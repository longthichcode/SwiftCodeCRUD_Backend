package com.project.Security.Controller;

import com.project.Security.DTO.AuthRequest;
import com.project.Security.DTO.UserDetailDTO;
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
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<?> createUser(@RequestParam String username, 
                                       @RequestParam String password, 
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
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, 
                                       @RequestParam(required = false) String username, 
                                       @RequestParam(required = false) String password, 
                                       @RequestParam(required = false) String roleName) {
        try {
            UserEntity user = userService.updateUser(id, username, password, roleName);
            return ResponseEntity.ok("User updated: " + user.getUSER_NAME());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Chỉnh sửa role người dùng
    @PutMapping("/{userId}/update-roles")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<?> updateUserRoles(@PathVariable Integer userId, @RequestBody List<String> roleNames) {
        try {
            // Loại bỏ tiền tố "ROLE_" từ tất cả các roleNames
            List<String> cleanedRoleNames = roleNames.stream()
                .map(role -> role.replace("ROLE_", ""))
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

    // Chỉnh sửa trạng thái người dùng
    @PutMapping("/{userId}/update-status")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
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
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable Integer userId) {
        try {
            List<String> roleNames = userService.getUserRoles(userId);
            return ResponseEntity.ok(roleNames);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Collections.emptyList());
        }
    }

    // Lấy tất cả vai trò
    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<List<String>> getAllRoles() {
    	try {
    		List<RoleEntity> roles = userService.getAllRoles();
    		List<String> roleName = new ArrayList<String>();
    		for (RoleEntity roleEntity : roles) {
				roleName.add("ROLE_"+roleEntity.getROLE_NAME());
			}
			return ResponseEntity.ok(roleName);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Collections.emptyList());
		}
	}	
    
//    // Lấy tất cả vai trò và quyền tương ứng
// 	@GetMapping("/roles/permissions")
// 	@PreAuthorize("hasAuthority('MANAGE_ROLES')")
// 	public ResponseEntity<List<PermissionAndRoleResponse>> getAllRolesAndPermission() {
// 		List<RoleEntity> roles = userService.getAllRoles();
// 		List<PermissionAndRoleResponse> roleResponses = new ArrayList<>();
//
// 		for (RoleEntity role : roles) {
// 			PermissionAndRoleResponse response = new PermissionAndRoleResponse();
// 			response.setId(role.getID());
// 			response.setRoleName(role.getROLE_NAME());
// 			List<Permission> permissions = userService.getPermissionsForRole(role.getID());
// 			List<String> pername = new ArrayList<String>();
// 			List<String> description = new ArrayList<String>();
// 			for (Permission permission : permissions) {
//				pername.add(permission.getName());
//				description.add(permission.getDescription() );
//			}
// 			response.setPermissions(pername);
// 			response.setDescription(description);
// 			response.setUsers(userService.findUsersByRoleId(role.getID()));
// 			roleResponses.add(response);
// 		}
//
// 		return ResponseEntity.ok(roleResponses);
// 	}
    

    //lấy tất cả người dùng
    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<List<UserDetailDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

//    // Lấy danh sách quyền theo vai trò
//    @GetMapping("/roles/permissions/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
//    public ResponseEntity<?> getPermissionsForRole(@PathVariable int id) {
//        try {
//            List<String> permissions = userService.getPermissionsForRole(id).stream()
//				.map(Permission::getName)
//				.collect(Collectors.toList());
//            return ResponseEntity.ok(permissions);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(400).body(e.getMessage());
//        }
//    }
    
//    // Sửa quyền của vai trò
//    @PutMapping("/roles/{roleId}/permissions")
//    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
//    public ResponseEntity<?> updateRolePermissions(@PathVariable Integer roleId, @RequestBody List<String> permissions) {
//		try {
//			userService.updateRolePermissions(roleId, permissions);
//			Map<String, String> response = new HashMap<>();
//			response.put("message", "Role permissions updated successfully");
//			return ResponseEntity.ok(response);
//		} catch (IllegalArgumentException e) {
//			Map<String, String> errorResponse = new HashMap<>();
//			errorResponse.put("error", e.getMessage());
//			return ResponseEntity.status(400).body(errorResponse);
//		}
//	}
    // Lấy tất cả quyền
    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<List<String>> getAllPermissions() {
		try {
			List<Permission> permissions = userService.getAllPermissions();
			List<String> permissionNames = permissions.stream()
					.map(Permission::getName)
					.collect(Collectors.toList());
			return ResponseEntity.ok(permissionNames);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Collections.emptyList());
		}
	}
}