package com.project.Service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.DAO.SwiftCodeDAO;
import com.project.DTO.SwiftCodeDTO;
import com.project.Entity.SwiftCodeEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
		// TODO Auto-generated method stub
		List<SwiftCodeEntity> listSce = this.swiftCodeDAO.findBySpecification(sce);
		List<SwiftCodeDTO> listScd = new ArrayList<SwiftCodeDTO>();
		for (SwiftCodeEntity swiftCodeEntity : listSce) {
			listScd.add(maptoDTO(swiftCodeEntity));
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
}
