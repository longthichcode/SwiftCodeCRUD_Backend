package com.project.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.project.DTO.SwiftCodeDTO;
import com.project.Entity.SwiftCodeEntity;
import com.project.Repository.SwiftCodeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service("JDService")
public class SwiftCodeIServiceJPADynamic implements SwiftCodeService {

	public final SwiftCodeRepository repository;
	public final EntityManager entityManager;

	public SwiftCodeIServiceJPADynamic(SwiftCodeRepository repository,EntityManager entityManager) {
		this.repository = repository;
		this.entityManager = entityManager ;
	}

	@Override
	public SwiftCodeDTO findByID(int id) {
		// TODO Auto-generated method stub
		Optional<SwiftCodeEntity> sce = this.repository.findById(id);
		if (sce.isEmpty()) {
			return null;
		} else {
			return maptoDTO(sce.get());
		}
	}

	@Override
    public List<SwiftCodeDTO> findBySpe(SwiftCodeEntity sce) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_search_swift_code", SwiftCodeEntity.class);

        query.registerStoredProcedureParameter("p_bank_type", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_bank_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_branch", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_country_code", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

        query.setParameter("p_bank_type", sce.getBANK_TYPE());
        query.setParameter("p_bank_name", sce.getBANK_NAME());
        query.setParameter("p_branch", sce.getBRANCH());
        query.setParameter("p_city", sce.getCITY());
        query.setParameter("p_country_code", sce.getCOUNTRY_CODE());

        List<SwiftCodeEntity> listSce = query.getResultList();

        // Chuyển đổi sang SwiftCodeDTO
        List<SwiftCodeDTO> listScd = new ArrayList<>();
        for (SwiftCodeEntity swiftCodeEntity : listSce) {
            SwiftCodeDTO scd = maptoDTO(swiftCodeEntity);
            listScd.add(scd);
        }

        return listScd;
    }
//	@Override
//	public List<SwiftCodeDTO> findBySpe(SwiftCodeEntity sce) {
//		// TODO Auto-generated method stub
//		List<SwiftCodeEntity> listSce = this.repository.findAll(new Specification<SwiftCodeEntity>() {
//
//			@Override
//			public Predicate toPredicate(Root<SwiftCodeEntity> root, CriteriaQuery<?> query,
//					CriteriaBuilder criteriaBuilder) {
//				// TODO Auto-generated method stub
//				List<Predicate> listPre = new ArrayList<Predicate>();
//				if (sce.getBANK_TYPE() != null) {
//					listPre.add(criteriaBuilder.like(root.get("BANK_TYPE"), "%" + sce.getBANK_TYPE() + "%"));
//				}
//				if (sce.getBANK_NAME() != null) {
//					listPre.add(criteriaBuilder.like(root.get("BANK_NAME"), "%" + sce.getBANK_NAME() + "%"));
//				}
//				if (sce.getBRANCH() != null) {
//					listPre.add(criteriaBuilder.like(root.get("BRANCH"), "%" + sce.getBRANCH() + "%"));
//				}
//				if (sce.getCITY() != null) {
//					listPre.add(criteriaBuilder.like(root.get("CITY"), "%" + sce.getCITY() + "%"));
//				}
//				if (sce.getCOUNTRY_CODE() != null) {
//					listPre.add(criteriaBuilder.like(root.get("COUNTRY_CODE"), "%" + sce.getCOUNTRY_CODE() + "%"));
//				}
//				Predicate[] arrPre = new Predicate[listPre.size()];
//				for (int i = 0; i < listPre.size(); i++) {
//					arrPre[i] = listPre.get(i);
//				}
//				return criteriaBuilder.and(arrPre);
//			}
//		});
//		List<SwiftCodeDTO> listScd = new ArrayList<SwiftCodeDTO>();
//		for (SwiftCodeEntity swiftCodeEntity : listSce) {
//			SwiftCodeDTO scd = maptoDTO(swiftCodeEntity);
//			listScd.add(scd);
//		}
//
//		return listScd;
//	}

	@Override
	public SwiftCodeDTO add(SwiftCodeDTO scd) {
		// TODO Auto-generated method stub
		if (scd == null) {
			throw new RuntimeException("SwiftCodeDTO không được null");
		}
		SwiftCodeDTO scodeDTO = this.findByID(scd.getID());
		if(scodeDTO!=null) {
			SwiftCodeDTO newScd = new SwiftCodeDTO();
			newScd.setACTIVE_STATUS(scd.getACTIVE_STATUS());
			newScd.setADDRESS_1(scd.getADDRESS_1());
			newScd.setADDRESS_2(scd.getADDRESS_2());
			newScd.setADDRESS_3(scd.getADDRESS_3());
			newScd.setADDRESS_4(scd.getADDRESS_4());
			newScd.setBANK_NAME(scd.getBANK_NAME());
			newScd.setBANK_TYPE(scd.getBANK_TYPE());
			newScd.setBRANCH(scd.getBRANCH());
			newScd.setCITY(scd.getCITY());
			newScd.setCONNECTED_TO_SWIFT(scd.getCONNECTED_TO_SWIFT());
			newScd.setCOUNTRY_CODE(scd.getCOUNTRY_CODE());
			newScd.setJSON_DATA(scd.getJSON_DATA());
			newScd.setPARA_STATUS(scd.getPARA_STATUS());
			newScd.setSWIFT_CODE(scd.getSWIFT_CODE());
			newScd.setSWIFT_CONNECTION(scd.getSWIFT_CONNECTION());
			SwiftCodeEntity swiftCode = maptoEntity(newScd);
			this.repository.save(swiftCode);
			return newScd ;
		}
		SwiftCodeEntity swiftCode = maptoEntity(scd);
		this.repository.save(swiftCode);
		return scd;
	}

	@Override
	public SwiftCodeDTO update(int id, SwiftCodeDTO scd) {
		// TODO Auto-generated method stub
		if (scd == null) {
			throw new RuntimeException("SwiftCodeDTO không được null");
		}
		Optional<SwiftCodeEntity> opt = this.repository.findById(id);
		if (opt.isEmpty()) {
			throw new RuntimeException("SwiftCode không tìm được ID: " + id);
		}

		SwiftCodeEntity before = opt.get();

		SwiftCodeEntity after = maptoEntity(scd);
		after.setID(before.getID());
		this.repository.save(after);
		return scd;
	}

	@Override
	public void delete(SwiftCodeEntity sce) {
		// TODO Auto-generated method stub
		Optional<SwiftCodeEntity> opt = this.repository.findById(sce.getID());
		if (opt.isEmpty()) {
			throw new RuntimeException("swiftcode không tìm được id : " + sce.getID());
		} else {
			this.repository.deleteById(opt.get().getID());
		}
	}

	@Override
	public Page<SwiftCodeDTO> paging(SwiftCodeEntity sf, Pageable page) {
		// TODO Auto-generated method stub
		int page_number = page.getPageNumber();
		int page_size = page.getPageSize();

		List<SwiftCodeDTO> data = this.findBySpe(sf);
		int count = data.size();

		int start = page_number * page_size;
		int end = start + page_size;

		if (start > count) {
			start = count;
		}
		if (end > count) {
			end = count;
		}

		List<SwiftCodeDTO> dataInPage;
		if (start == end) {
			dataInPage = new ArrayList<>();
		} else {
			dataInPage = data.subList(start, end);
		}
		return new PageImpl<SwiftCodeDTO>(dataInPage, page, count);
	}

	public static SwiftCodeDTO maptoDTO(SwiftCodeEntity sce) {
		return new SwiftCodeDTO(sce.getID(), sce.getBANK_TYPE(), sce.getSWIFT_CODE(), sce.getCONNECTED_TO_SWIFT(),
				sce.getSWIFT_CONNECTION(), sce.getBANK_NAME(), sce.getBRANCH(), sce.getADDRESS_1(),
				sce.getADDRESS_2(), sce.getADDRESS_3(), sce.getADDRESS_4(), sce.getCITY(), sce.getCOUNTRY_CODE(),
				sce.getPARA_STATUS(), sce.getACTIVE_STATUS(), sce.getJSON_DATA());
	}

	public static SwiftCodeEntity maptoEntity(SwiftCodeDTO scd) {
		return new SwiftCodeEntity(scd.getID(), scd.getBANK_TYPE(), scd.getSWIFT_CODE(), scd.getCONNECTED_TO_SWIFT(),
				scd.getSWIFT_CONNECTION(), scd.getBANK_NAME(), scd.getBRANCH(), scd.getADDRESS_1(),
				scd.getADDRESS_2(), scd.getADDRESS_3(), scd.getADDRESS_4(), scd.getCITY(), scd.getCOUNTRY_CODE(),
				scd.getPARA_STATUS(), scd.getACTIVE_STATUS(), scd.getJSON_DATA());
	}

}
