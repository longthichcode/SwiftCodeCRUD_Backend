package com.project.Security.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Security.Entity.FunctionEntity;

public interface FunctionRepository extends JpaRepository<FunctionEntity, Integer> {
		// Tìm chức năng theo tên
	FunctionEntity findByName(String functionName);

	// Kiểm tra xem chức năng có tồn tại không
	boolean existsByName(String functionName);
	
	// Lấy tất cả chức năng
	List<FunctionEntity> findAll();
}
