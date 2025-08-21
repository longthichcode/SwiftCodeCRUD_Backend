package com.project.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.project.Entity.SwiftCodeEntity;

public interface SwiftCodeRepository extends CrudRepository<SwiftCodeEntity, Integer>,JpaSpecificationExecutor<SwiftCodeEntity>{

	@Query("SELECT s FROM SwiftCodeEntity s WHERE s.SWIFT_CODE = ?1")
	Optional<SwiftCodeEntity> findBySWIFTCODE(String swift_CODE);
}
