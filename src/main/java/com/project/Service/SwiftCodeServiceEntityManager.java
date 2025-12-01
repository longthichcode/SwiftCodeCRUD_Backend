package com.project.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.DAO.SwiftCodeDAO;
import com.project.DTO.ExcelError;
import com.project.DTO.SwiftCodeDTO;
import com.project.Entity.SwiftCodeEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;

@Service("EMService")
public class SwiftCodeServiceEntityManager implements SwiftCodeService {

	public final SwiftCodeDAO swiftCodeDAO;
    @SuppressWarnings("unused")
	private final SwiftCodeProcedureCaller procedureCaller;

    @PersistenceContext
    private EntityManager entityManager;

    public SwiftCodeServiceEntityManager(SwiftCodeDAO swiftCodeDAO) {
        this.swiftCodeDAO = swiftCodeDAO;
        this.procedureCaller = new SwiftCodeProcedureCaller(entityManager);
    }

	@Override
	public SwiftCodeDTO findByID(int id) {
		// TODO Auto-generated method stub
		SwiftCodeEntity sce = this.swiftCodeDAO.findById(id);
		if (sce != null) {
			return maptoDTO(sce);
		} else {
			throw new RuntimeException("Lỗi ở findById Service");
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
        System.out.println("List size: " + listSce.size());
        for (SwiftCodeEntity swiftCodeEntity : listSce) {
            SwiftCodeDTO scd = maptoDTO(swiftCodeEntity);
            listScd.add(scd);
        }

        return listScd;
    }

	@Override
	@Transactional
	public SwiftCodeDTO add(SwiftCodeDTO scd) {
		// TODO Auto-generated method stub
		if (scd == null) {
			throw new RuntimeException("SwiftCodeDTO không được null");
		}
		SwiftCodeEntity swiftCode = maptoEntity(scd);
		return maptoDTO(this.swiftCodeDAO.save(swiftCode));
	}

	@Override
	public SwiftCodeDTO update(int id, SwiftCodeDTO scd) {
		// TODO Auto-generated method stub
		if (scd == null) {
			throw new RuntimeException("SwiftCodeDTO không được null");
		}
		SwiftCodeEntity swiftCode = this.swiftCodeDAO.findById(id);
		if (swiftCode != null) {
			SwiftCodeEntity updatedSwiftCode = maptoEntity(scd);
			updatedSwiftCode.setID(swiftCode.getID());

			return maptoDTO(this.swiftCodeDAO.save(updatedSwiftCode));
		} else {
			throw new RuntimeException("SwiftCode not found with ID: " + id);
		}
	}

	@Override
	public void delete(SwiftCodeEntity sce) {
		SwiftCodeEntity swiftCode = this.swiftCodeDAO.findById(sce.getID());
		if (swiftCode != null) {
			this.swiftCodeDAO.deleteById(swiftCode.getID());
		} else {
			throw new RuntimeException("service không tìm dượcd id : " + sce.getID());
		}
	}

	@Override
	public Page<SwiftCodeDTO> paging(SwiftCodeEntity sf, Pageable page) {
		// TODO Auto-generated method stub

		int page_number = page.getPageNumber();
		int page_size = page.getPageSize();

		int start = page_number * page_size;
		int end = start + page_size;

		List<SwiftCodeDTO> data = this.findBySpe(sf);
		int count = data.size();
		if (start > count) {
			start = count;
		}
		if (end > count) {
			end = count;
		}

		List<SwiftCodeDTO> dataInPage = data.subList(start, end) ;
		return new PageImpl<SwiftCodeDTO>(dataInPage, page, count);
	}

	public static SwiftCodeDTO maptoDTO(SwiftCodeEntity sce) {
		return new SwiftCodeDTO(sce.getID(), sce.getBANK_TYPE(), sce.getSWIFT_CODE(), sce.getCONNECTED_TO_SWIFT(),
				sce.getSWIFT_CONNECTION(), sce.getBANK_NAME(), sce.getBRANCH(), sce.getADDRESS_1(), sce.getADDRESS_2(),
				sce.getADDRESS_3(), sce.getADDRESS_4(), sce.getCITY(), sce.getCOUNTRY_CODE(), sce.getPARA_STATUS(),
				sce.getACTIVE_STATUS(), sce.getJSON_DATA());
	}

	public static SwiftCodeEntity maptoEntity(SwiftCodeDTO scd) {
		return new SwiftCodeEntity(scd.getID(), scd.getBANK_TYPE(), scd.getSWIFT_CODE(), scd.getCONNECTED_TO_SWIFT(),
				scd.getSWIFT_CONNECTION(), scd.getBANK_NAME(), scd.getBRANCH(), scd.getADDRESS_1(), scd.getADDRESS_2(),
				scd.getADDRESS_3(), scd.getADDRESS_4(), scd.getCITY(), scd.getCOUNTRY_CODE(), scd.getPARA_STATUS(),
				scd.getACTIVE_STATUS(), scd.getJSON_DATA());
	}

	@Override
	public ByteArrayInputStream exportToExcel(List<SwiftCodeEntity> swiftCodes) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> importFromExcel(InputStream inp) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArrayInputStream exportToErrorExcel(List<SwiftCodeEntity> swiftCodes, List<ExcelError> errors)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
