package com.project.Security.Service;

import com.project.Security.DTO.FunctionResponse;
import com.project.Security.DTO.PermissionDTO;
import com.project.Security.DTO.UserDetailDTO;
import com.project.Security.Entity.FunctionEntity;
import com.project.Security.Entity.Permission;
import com.project.Security.Entity.RoleEntity;
import com.project.Security.Entity.UserEntity;
import com.project.Security.Repository.FunctionRepository;
import com.project.Security.Repository.RoleRepository;
import com.project.Security.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private FunctionRepository functionRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * <i>Phương thức này dùng để tạo người dùng mới trong hệ thống.</i>
	 * 
	 * @param username Tên đăng nhập của người dùng mới.
	 * @param password Mật khẩu của người dùng mới.
	 * @param roleName Tên vai trò của người dùng mới. Nếu vai trò không tồn tại, sẽ
	 *                 ném ra ngoại lệ.
	 */
	@Transactional
	public UserEntity createUser(String username, String password, String roleName) {
		// Kiểm tra xem username đã tồn tại chưa
		if (userRepository.existsByUSER_NAME(username) == 1) {
			throw new IllegalArgumentException("Username đã tồn tại trong hệ thống : " + username);
		}

		// Kiểm tra xem roleName có tồn tại không
		RoleEntity role = roleRepository.findByROLE_NAME(roleName);
		if (role == null) {
			throw new IllegalArgumentException("Role not found: " + roleName);
		}

		// Tạo người dùng mới
		UserEntity user = new UserEntity();
		user.setUSER_NAME(username);
		user.setPASSWORD(passwordEncoder.encode(password));
		user.setENABLED(true);

		// Gán vai trò
		Set<RoleEntity> roles = new HashSet<>();
		roles.add(role);
		user.setROLES(roles);

		// Lưu người dùng
		return userRepository.save(user);
	}

	/**
	 * <i>Phương thức này dùng để cập nhật vai trò của người dùng.</i>
	 * 
	 * @param userId    ID của người dùng cần cập nhật.
	 * @param roleNames Danh sách tên vai trò mới mà người dùng sẽ được gán. Nếu vai
	 *                  trò không tồn tại, sẽ ném ra ngoại lệ.
	 */
	@Transactional
	public void updateUserRoles(Integer userId, List<String> roleNames) {
		Optional<UserEntity> userOpt = userRepository.findById(userId);
		if (!userOpt.isPresent()) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}

		UserEntity user = userOpt.get();
		user.getROLES().clear();
		Set<RoleEntity> newRoles = new HashSet<>();
		for (String roleName : roleNames) {
			RoleEntity role = roleRepository.findByROLE_NAME(roleName);
			if (role == null) {
				throw new IllegalArgumentException("Role not found: " + roleName);
			}
			newRoles.add(role);
		}
		user.setROLES(newRoles);
		userRepository.save(user);
	}

	/**
	 * <i>Phương thức này dùng để tìm người dùng theo tên đăng nhập.</i>
	 * 
	 * @param username Tên đăng nhập của người dùng cần tìm. Nếu không tìm thấy
	 *                 người dùng, sẽ ném ra ngoại lệ.
	 */
	public UserEntity findByUsername(String username) {
		return userRepository.findByUsername(username).get();
	}

	/**
	 * <i>Phương thức này dùng để cập nhật thông tin người dùng.</i>
	 * @param userId    ID của người dùng cần cập nhật.
	 * @param username Tên đăng nhập mới của người dùng. Nếu không muốn cập nhật,truyền vào null hoặc chuỗi rỗng.
	 * @param password Mật khẩu mới của người dùng. Nếu không muốn cập nhật, truyền vào null hoặc chuỗi rỗng.
	 * @param roleName Tên vai trò mới của người dùng. Nếu không muốn cập nhật, truyền vào null hoặc chuỗi rỗng.
	 */
	@Transactional
	public UserEntity updateUser(Integer userId, String username, String password, String roleName) {
		Optional<UserEntity> userOpt = userRepository.findById(userId);
		if (!userOpt.isPresent()) {
			throw new IllegalArgumentException("User not found");
		}

		UserEntity user = userOpt.get();
		if (username != null && !username.isEmpty()) {
			if (userRepository.existsByUSER_NAME(username) == 1 && !username.equals(user.getUSER_NAME())) {
				throw new IllegalArgumentException("Username already exists");
			}
			user.setUSER_NAME(username);
		}
		if (password != null && !password.isEmpty()) {
			user.setPASSWORD(passwordEncoder.encode(password));
		}
		if (roleName != null && !roleName.isEmpty()) {
			if (!roleRepository.existsByROLE_NAME(roleName)) {
				throw new IllegalArgumentException("Role not found: " + roleName);
			}
			userRepository.editRole(userId, roleName);
		}
		return userRepository.save(user);
	}

	/**
	 * <i>Phương thức này dùng để cập nhật trạng thái của người dùng.</i>
	 * @param userId ID của người dùng cần cập nhật trạng thái.
	 * @param status Trạng thái mới của người dùng (1 cho kích hoạt, 0 cho vô hiệu hóa).
	 * 			 Nếu người dùng không tồn tại, sẽ ném ra ngoại lệ.
	 */
	@Transactional
	public void updateUserStatus(Integer userId, int status) {
		Optional<UserEntity> userOpt = userRepository.findById(userId);
		if (!userOpt.isPresent()) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}
		userRepository.editStatus(userId, status);
	}

	/**
	 * <i>Phương thức này dùng để lấy danh sách vai trò của người dùng.</i>
	 * @param userId ID của người dùng cần lấy vai trò. Nếu người dùng không tồn tại, sẽ ném ra ngoại lệ.
	 * @return Danh sách các vai trò của người dùng, mỗi vai trò được định dạng là "ROLE_{roleName}".
	 */
	public List<String> getUserRoles(Integer userId) {
		Optional<UserEntity> userOpt = userRepository.findById(userId);
		if (!userOpt.isPresent()) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}
		UserEntity user = userOpt.get();
		return user.getROLES().stream().map(role -> "ROLE_" + role.getROLE_NAME()).distinct()
				.collect(Collectors.toList());
	}

	/**
	 * <i>Phương thức này dùng để lấy tất cả vai trò trong hệ thống.</i>
	 * @return Danh sách tất cả các vai trò, mỗi vai trò là một đối tượng RoleEntity.
	 */
	public List<RoleEntity> getAllRoles() {
		return roleRepository.findAll();
	}

	// Lấy tất cả người dùng dưới dạng UserDetailDTO
	public List<UserDetailDTO> getAllUsers() {
		return userRepository.findAllUsers().stream().map(user -> new UserDetailDTO(user.getID(), user.getUSER_NAME(),
				user.getPASSWORD(), user.getENABLED() ? 1 : 0)).collect(Collectors.toList());
	}
	
	// xoá người dùng theo ID 
	
	@Transactional
	public boolean deleteUserById(int userId) {
		if (!userRepository.existsById(userId)) {
			return false;
		}else {
			userRepository.deleteUserRoleByUserId(userId);
			userRepository.deleteUserById(userId);
			return true;
		}
		
	}
	
	// Tìm người dùng theo vai trò
	public List<String> findUsersByRoleId(Integer roleId) {
		return roleRepository.findUsersByRoleId(roleId);
	}

	//tìm vai trò theo người dùng
	public List<String> findRolesByUserId(Integer userId) {
		return userRepository.findRolesByUserId(userId);
	}
	
	//Lấy tất cả vai trò 
	public List<RoleEntity> getAllRoleEntities() {
		return roleRepository.findAll();
	}
	
	//Lấy tất cả chức năng theo vai trò
	public List<FunctionEntity> getFunctionByRoleId(Integer roleId) {
		return functionRepository.findByRolesId(roleId);
	}
	
	// Sửa chức năng của vai trò
	@Transactional
	public void editFunctionsByRoleId(Integer roleId, List<String> functionNames) {
	    roleRepository.deleteFunctionsByRoleId(roleId);
	    if (functionNames != null && !functionNames.isEmpty()) {
	        roleRepository.insertFunctions(roleId, functionNames);
	    }
	}
	
	public List<FunctionResponse> getAllFunctions() {
        List<FunctionEntity> functions = functionRepository.findAllWithPermissions();
        return functions.stream()
            .map(f -> new FunctionResponse(
                f.getId(),
                f.getName(),
                f.getDescription(),
                f.getPermissions().stream()
                    .map(p -> new PermissionDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getUrl(),
                        p.getMethod()
                    ))
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    }
	
	// Lấy tất cả quyền
	public List<Permission> getAllPermissions() {
		return roleRepository.findAllPermissions();
	}
	// lấy tất cả quyền thep chức năng 
	public List<Permission> getPermissionsByFunctionId(Integer functionId) {
		return functionRepository.findPermissionsByFunctionId(functionId);
	}
	
	// Lất tất cả quyền theo vai trò không thông qua Function
	public List<Permission> getPermissionsByRoleId(Integer roleId) {
		return roleRepository.findPermissionsByRoleIdWithoutFunction(roleId);
	}
	
	//sửa quyền của vai trò
	@Transactional
	public void editPermissionsByRoleId(Integer roleId, List<String> permissionNames) {
		roleRepository.deletePermissionsByRoleId(roleId);
		if (permissionNames != null && !permissionNames.isEmpty()) {
			roleRepository.insertPermissions(roleId, permissionNames);
		}
	}
}