package com.project.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.project.Entity.SwiftCodeEntity;

public interface SwiftCodeRepository extends CrudRepository<SwiftCodeEntity, Integer>,JpaSpecificationExecutor<SwiftCodeEntity>{

}
