package com.project.Security.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.Security.Entity.FunctionEntity;
import com.project.Security.Entity.Permission;

public interface FunctionRepository extends JpaRepository<FunctionEntity, Integer> {
		// Tìm chức năng theo tên
	FunctionEntity findByName(String functionName);

	// Kiểm tra xem chức năng có tồn tại không
	boolean existsByName(String functionName);
	
	// Lấy tất cả chức năng
	List<FunctionEntity> findAll();
	
	@Query("SELECT f FROM FunctionEntity f LEFT JOIN FETCH f.permissions")
    List<FunctionEntity> findAllWithPermissions();
	
	//Lấy tất cả chức năng theo vai trò
	@Query(value = "SELECT f.* FROM SC_FUNCTIONS f " +
			"JOIN SC_ROLE_FUNCTION rf ON f.ID = rf.FUNCTION_ID " +
			"WHERE rf.ROLE_ID = :roleId", nativeQuery = true)
	List<FunctionEntity> findByRolesId(Integer roleId);
	
	//Lấy tất cả quyền theo chức năng
	@Query(value = "SELECT p.* FROM SC_PERMISSIONS p " +
			"JOIN SC_FUNCTION_PERMISSION fp ON p.ID = fp.PERMISSION_ID " +
			"WHERE fp.FUNCTION_ID = :functionId", nativeQuery = true)
	List<Permission> findPermissionsByFunctionId(Integer functionId);
}
