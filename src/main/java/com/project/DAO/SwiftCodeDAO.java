package com.project.DAO;

import java.util.List;

import com.project.Entity.SwiftCodeEntity;

public interface SwiftCodeDAO {
	SwiftCodeEntity findById(int id);
	List<SwiftCodeEntity> getAll() ;
	List<SwiftCodeEntity> findBySpecification(SwiftCodeEntity sce) ;
	void deleteById(int id) ;
	SwiftCodeEntity save(SwiftCodeEntity sce);
}
