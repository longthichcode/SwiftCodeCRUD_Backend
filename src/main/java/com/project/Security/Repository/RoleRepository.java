package com.project.Security.Repository;

import com.project.Security.Entity.FunctionEntity;
import com.project.Security.Entity.Permission;
import com.project.Security.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
	
	// Tìm vai trò theo tên
    @Query(value = "SELECT * FROM SC_ROLES WHERE ROLE_NAME = :roleName", nativeQuery = true)
    RoleEntity findByROLE_NAME(@Param("roleName") String roleName);

    // Kiểm tra xem vai trò có tồn tại không
    @Query(value = "SELECT COUNT(*) > 0 FROM SC_ROLES WHERE ROLE_NAME = :roleName", nativeQuery = true)
    boolean existsByROLE_NAME(@Param("roleName") String roleName);

    // Tìm các vai trò của người dùng dựa trên userId
    @Query(value = "SELECT r.* FROM SC_ROLES r JOIN SC_USER_ROLE ur ON r.ID = ur.ROLE_ID WHERE ur.USER_ID = :userId", nativeQuery = true)
    List<RoleEntity> findRolesByUserId(@Param("userId") Integer userId);

    // Lấy tất cả vai trò
    @Query(value = "SELECT * FROM SC_ROLES", nativeQuery = true)
    List<RoleEntity> findAll();
    
    // Lấy danh sách quyền theo vai trò (thông qua Function)
    @Query(value = "SELECT p.* FROM SC_PERMISSIONS p " +
                   "JOIN SC_FUNCTION_PERMISSION fp ON p.ID = fp.PERMISSION_ID " +
                   "JOIN SC_FUNCTIONS f ON fp.FUNCTION_ID = f.ID " +
                   "JOIN SC_ROLE_FUNCTION rf ON f.ID = rf.FUNCTION_ID " +
                   "WHERE rf.ROLE_ID = :roleId", nativeQuery = true)
    List<Permission> findPermissionsByRoleId(@Param("roleId") Integer roleId);

    // Lấy danh sách chức năng theo vai trò
    @Query(value = "SELECT f.* FROM SC_FUNCTIONS f " +
                   "JOIN SC_ROLE_FUNCTION rf ON f.ID = rf.FUNCTION_ID " +
                   "WHERE rf.ROLE_ID = :roleId", nativeQuery = true)
    List<FunctionEntity> findFunctionsByRoleId(@Param("roleId") Integer roleId);

    // Xóa chức năng của vai trò
    @Modifying
    @Query(value = "DELETE FROM SC_ROLE_FUNCTION WHERE ROLE_ID = :roleId", nativeQuery = true)
    void deleteFunctionsByRoleId(@Param("roleId") Integer roleId);

    // Thêm chức năng cho vai trò
    @Modifying
    @Query(value = "INSERT INTO SC_ROLE_FUNCTION (ROLE_ID, FUNCTION_ID) " +
                   "SELECT :roleId, f.ID FROM SC_FUNCTIONS f WHERE f.NAME IN :functions", nativeQuery = true)
    void insertFunctions(@Param("roleId") Integer roleId, @Param("functions") List<String> functions);

    // Tìm người dùng theo vai trò
    @Query(value = "SELECT u.USER_NAME FROM SC_USERS u JOIN SC_USER_ROLE ur ON u.ID = ur.USER_ID WHERE ur.ROLE_ID = :roleId", nativeQuery = true)
    List<String> findUsersByRoleId(@Param("roleId") Integer roleId);

    // Lấy tất cả quyền
    @Query(value = "SELECT * FROM SC_PERMISSIONS", nativeQuery = true)
    List<Permission> findAllPermissions();

    // Lấy tất cả chức năng
    @Query(value = "SELECT * FROM SC_FUNCTIONS", nativeQuery = true)
    List<FunctionEntity> findAllFunctions();
    
    //lấy tất cả quyền theo vai trò không thông qua Function
    @Query(value = "SELECT p.* FROM SC_PERMISSIONS p " +
				   "JOIN SC_ROLE_PERMISSION rp ON p.ID = rp.PERMISSION_ID " +
				   "WHERE rp.ROLE_ID = :roleId", nativeQuery = true)
    List<Permission> findPermissionsByRoleIdWithoutFunction(@Param("roleId") Integer roleId);
    
    // xoá quyền của vai trò 
    @Modifying
    @Query(value = "DELETE FROM SC_ROLE_PERMISSION WHERE ROLE_ID = :roleId", nativeQuery = true)
    void deletePermissionsByRoleId(@Param("roleId") Integer roleId);
    // Thêm quyền cho vai trò
    @Modifying
    @Query(value = "INSERT INTO SC_ROLE_PERMISSION (ROLE_ID, PERMISSION_ID) " +
				   "SELECT :roleId, p.ID FROM SC_PERMISSIONS p WHERE p.NAME IN :permissions", nativeQuery = true)
    void insertPermissions(@Param("roleId") Integer roleId, @Param("permissions") List<String> permissions);
    
    
}