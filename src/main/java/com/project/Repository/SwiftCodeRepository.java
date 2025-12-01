package com.project.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.project.Entity.SwiftCodeEntity;

import jakarta.transaction.Transactional;

public interface SwiftCodeRepository extends CrudRepository<SwiftCodeEntity, Integer>,JpaSpecificationExecutor<SwiftCodeEntity>{

	@Query("SELECT s FROM SwiftCodeEntity s WHERE s.SWIFT_CODE = ?1")
	Optional<SwiftCodeEntity> findBySWIFTCODE(String swift_CODE);
	
	//cập nhật trạng thái para_status()
	@Modifying
	@Transactional
	@Query("UPDATE SwiftCodeEntity s SET s.PARA_STATUS = ?2 WHERE s.ID = ?1")
	void updateParaStatus(int id, int para_status);
}
