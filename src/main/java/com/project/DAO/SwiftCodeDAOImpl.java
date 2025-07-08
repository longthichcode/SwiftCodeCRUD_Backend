package com.project.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.project.Entity.SwiftCodeEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class SwiftCodeDAOImpl implements SwiftCodeDAO {

	public final EntityManager entityManager;

	public SwiftCodeDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	

	@Override
	public SwiftCodeEntity findById(int id) {
		// TODO Auto-generated method stub
		SwiftCodeEntity sce = this.entityManager.find(SwiftCodeEntity.class, id);
		if (sce != null) {
			return sce;
		} else {
			throw new RuntimeException("không tìm được entity theo id");
		}
	}

	@Override
	public List<SwiftCodeEntity> getAll() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<SwiftCodeEntity> list = this.entityManager
				.createNativeQuery("SELECT * FROM PARA_SWIFT_CODE_NH", SwiftCodeEntity.class).getResultList();
		return list;
	}

	
	@Override
	public List<SwiftCodeEntity> findBySpecification(SwiftCodeEntity sce) {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder("SELECT * FROM PARA_SWIFT_CODE_NH WHERE 1=1 ");
		if (sce.getBANK_TYPE() != null) {
			str.append("AND BANK_TYPE LIKE :banktype ");
		}
		if (sce.getBANK_NAME() != null) {
			str.append("AND BANK_NAME LIKE :bankname ");
		}
		if (sce.getBRANCH() != null) {
			str.append("AND BRANCH LIKE :branch ");
		}
		if (sce.getCITY() != null) {
			str.append("AND CITY LIKE :city ");
		}
		if (sce.getCOUNTRY_CODE() != null) {
			str.append("AND COUNTRY_CODE LIKE :countrycode");
		}

		Query query = this.entityManager.createNativeQuery(str.toString(),SwiftCodeEntity.class);
		
		if (sce.getBANK_TYPE() != null) {
			query.setParameter("banktype", "%"+sce.getBANK_TYPE()+"%");
		}
		if (sce.getBANK_NAME() != null) {
			query.setParameter("bankname", "%"+sce.getBANK_NAME()+"%");
		}
		if (sce.getBRANCH() != null) {
			query.setParameter("branch", "%"+sce.getBRANCH()+"%");
		}
		if (sce.getCITY() != null) {
			query.setParameter("city", "%"+sce.getCITY()+"%");
		}
		if (sce.getCOUNTRY_CODE() != null) {
			query.setParameter("countrycode", "%"+sce.getCOUNTRY_CODE()+"%");
		}
		
		@SuppressWarnings("unchecked")
		List<SwiftCodeEntity> list  = query.getResultList() ;
		return list;
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		SwiftCodeEntity sce = this.findById(id);
		if (sce != null) {
			this.entityManager.remove(sce);
		} else {
			throw new RuntimeException("không xoá được đối tượng nào có id trên !");
		}
	}

	@Override
	public SwiftCodeEntity save(SwiftCodeEntity sce) {
		// TODO Auto-generated method stub
		this.entityManager.persist(sce);
		return sce;
	}

}
