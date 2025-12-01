package com.project.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.DTO.ExcelError;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.transaction.annotation.Transactional;

@Service("JDService")
public class SwiftCodeIServiceJPADynamic implements SwiftCodeService {

	public final SwiftCodeRepository repository;
	public final EntityManager entityManager;

	public SwiftCodeIServiceJPADynamic(SwiftCodeRepository repository, EntityManager entityManager) {
		this.repository = repository;
		this.entityManager = entityManager;
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
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_search_swift_code",
				SwiftCodeEntity.class);

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
		if (scodeDTO != null) {
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
			return newScd;
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
		// Tạo workbook và sheet
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("SwiftCodes");

		// Tạo hàng tiêu đề
		Row headerRow = sheet.createRow(0);
		String[] columns = { "ID", "Bank Type", "Swift Code", "Connected to Swift", "Swift Connection", "Bank Name",
				"Branch", "Address 1", "Address 2", "Address 3", "Address 4", "City", "Country Code", "Para Status",
				"Active Status", "JSON Data" };

		// Ghi tiêu đề vào sheet
		for (int i = 0; i < columns.length; i++) {
			headerRow.createCell(i).setCellValue(columns[i]);
		}

		// Ghi dữ liệu vào sheet
		int rowNum = 1;
		for (SwiftCodeEntity sce : swiftCodes) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(sce.getID());
			row.createCell(1).setCellValue(sce.getBANK_TYPE() != null ? sce.getBANK_TYPE() : "");
			row.createCell(2).setCellValue(sce.getSWIFT_CODE() != null ? sce.getSWIFT_CODE() : "");
			row.createCell(3).setCellValue(sce.getCONNECTED_TO_SWIFT() != null ? sce.getCONNECTED_TO_SWIFT() : "");
			row.createCell(4).setCellValue(sce.getSWIFT_CONNECTION() != null ? sce.getSWIFT_CONNECTION() : "");
			row.createCell(5).setCellValue(sce.getBANK_NAME() != null ? sce.getBANK_NAME() : "");
			row.createCell(6).setCellValue(sce.getBRANCH() != null ? sce.getBRANCH() : "");
			row.createCell(7).setCellValue(sce.getADDRESS_1() != null ? sce.getADDRESS_1() : "");
			row.createCell(8).setCellValue(sce.getADDRESS_2() != null ? sce.getADDRESS_2() : "");
			row.createCell(9).setCellValue(sce.getADDRESS_3() != null ? sce.getADDRESS_3() : "");
			row.createCell(10).setCellValue(sce.getADDRESS_4() != null ? sce.getADDRESS_4() : "");
			row.createCell(11).setCellValue(sce.getCITY() != null ? sce.getCITY() : "");
			row.createCell(12).setCellValue(sce.getCOUNTRY_CODE() != null ? sce.getCOUNTRY_CODE() : "");
			row.createCell(13)
					.setCellValue(sce.getPARA_STATUS() != 0 ? sce.getPARA_STATUS() == 0 ? "Inactive" : "Active" : "");
			row.createCell(14).setCellValue(
					sce.getACTIVE_STATUS() != 0 ? sce.getACTIVE_STATUS() == 0 ? "Inactive" : "Active" : "");
			row.createCell(15).setCellValue(sce.getJSON_DATA() != null ? sce.getJSON_DATA() : "");
		}

		// Tự động điều chỉnh kích thước cột
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Ghi workbook vào output stream
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();

		return new ByteArrayInputStream(out.toByteArray());
	}
//
//	@Transactional
//	public Map<String, Object> importFromExcel(InputStream inp) throws IOException {
//		Map<String, Object> response = new HashMap<>();
//		List<ExcelError> errors = new ArrayList<>();
//		List<SwiftCodeEntity> validSwiftCodes = new ArrayList<>();
//		int skippedCount = 0;
//		int insertedCount = 0;
//
//		try (Workbook workbook = new XSSFWorkbook(inp)) {
//			Sheet sheet = workbook.getSheetAt(0);
//			Iterator<Row> rowIterator = sheet.iterator();
//
//			// Bỏ qua hàng tiêu đề
//			if (rowIterator.hasNext()) {
//				rowIterator.next();
//			}
//
//			// Duyệt qua các hàng
//			int rowNum = 1; // Bắt đầu từ hàng thứ 2 (sau tiêu đề)
//			while (rowIterator.hasNext()) {
//				Row row = rowIterator.next();
//				rowNum++;
//
//				SwiftCodeDTO scd = new SwiftCodeDTO();
//				boolean hasError = false;
//
//				scd.setBANK_TYPE(getCellStringValue(row.getCell(0)));
//				if (scd.getBANK_TYPE() == null || scd.getBANK_TYPE().isEmpty()) {
//					errors.add(new ExcelError(rowNum, 0, "Loại ngân hàng không được để trống"));
//					skippedCount++;
//					hasError = true;
//				}
//
//				scd.setSWIFT_CODE(getCellStringValue(row.getCell(1)));
//				if (scd.getSWIFT_CODE() == null || scd.getSWIFT_CODE().isEmpty()) {
//					errors.add(new ExcelError(rowNum, 1, "Mã SWIFT không được để trống"));
//					skippedCount++;
//					hasError = true;
//				} else if (scd.getSWIFT_CODE().length() < 8 || scd.getSWIFT_CODE().length() > 11) {
//					errors.add(new ExcelError(rowNum, 1, "Mã SWIFT phải có độ dài từ 8 đến 11 ký tự"));
//					skippedCount++;
//					hasError = true;
//				}
//
//				scd.setCONNECTED_TO_SWIFT(getCellStringValue(row.getCell(2)));
//				scd.setSWIFT_CONNECTION(getCellStringValue(row.getCell(3)));
//				scd.setBANK_NAME(getCellStringValue(row.getCell(4)));
//				if (scd.getBANK_NAME() == null || scd.getBANK_NAME().isEmpty()) {
//					errors.add(new ExcelError(rowNum, 4, "Tên ngân hàng không được để trống"));
//					skippedCount++;
//					hasError = true;
//				}
//
//				scd.setBRANCH(getCellStringValue(row.getCell(5)));
//				scd.setADDRESS_1(getCellStringValue(row.getCell(6)));
//				scd.setADDRESS_2(getCellStringValue(row.getCell(7)));
//				scd.setADDRESS_3(getCellStringValue(row.getCell(8)));
//				scd.setADDRESS_4(getCellStringValue(row.getCell(9)));
//				scd.setCITY(getCellStringValue(row.getCell(10)));
//				scd.setCOUNTRY_CODE(getCellStringValue(row.getCell(11)));
//				if (scd.getCOUNTRY_CODE() == null || scd.getCOUNTRY_CODE().isEmpty()
//						|| scd.getCOUNTRY_CODE().length() != 2) {
//					errors.add(new ExcelError(rowNum, 11, "Mã quốc gia không được để trống và phải có độ dài 2 ký tự"));
//					skippedCount++;
//					hasError = true;
//				}
//
//				String paraStatus = getCellStringValue(row.getCell(12));
//				scd.setPARA_STATUS("Active".equalsIgnoreCase(paraStatus) ? 1 : 0);
//
//				String activeStatus = getCellStringValue(row.getCell(13));
//				scd.setACTIVE_STATUS("Active".equalsIgnoreCase(activeStatus) ? 1 : 0);
//
//				scd.setJSON_DATA(getCellStringValue(row.getCell(14)));
//
//				// Kiểm tra xem SWIFT_CODE đã tồn tại chưa
//				if (!hasError) {
//					Optional<SwiftCodeEntity> existing = repository.findBySWIFTCODE(scd.getSWIFT_CODE());
//					if (existing.isPresent()) {
//						errors.add(new ExcelError(rowNum, 1, "Mã SWIFT đã tồn tại: " + scd.getSWIFT_CODE()));
//						skippedCount++;
//						hasError = true;
//					}
//				}
//
//				// Nếu không có lỗi, thêm vào danh sách hợp lệ
//				if (!hasError) {
//					validSwiftCodes.add(maptoEntity(scd));
//					insertedCount++;
//				}
//			}
//
//			// Lưu các bản ghi hợp lệ vào cơ sở dữ liệu
//			if (!validSwiftCodes.isEmpty()) {
//				repository.saveAll(validSwiftCodes);
//			}
//
//			// Chuẩn bị phản hồi
//			response.put("success", errors.isEmpty());
//			response.put("errors", errors);
//			response.put("insertedCount", insertedCount);
//			response.put("skippedCount", skippedCount);
//
//		} catch (Exception e) {
//			response.put("success", false);
//			response.put("errors", List.of(new ExcelError(0, 0, "Lỗi xử lý file Excel: " + e.getMessage())));
//		}
//
//		return response;
//	}
//	
//	
//
	// Hàm hỗ trợ lấy giá trị chuỗi từ ô
	private String getCellStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			return String.valueOf((int) cell.getNumericCellValue());
		default:
			return null;
		}
	}

	@Transactional
	public Map<String, Object> importFromExcel(InputStream inp) throws IOException {
	    Map<String, Object> response = new HashMap<>();
	    List<SwiftCodeEntity> validSwiftCodes = new ArrayList<>();
	    List<List<String>> errorRows = new ArrayList<>();

	    int skippedCount = 0;
	    int insertedCount = 0;

	    try (Workbook workbook = new XSSFWorkbook(inp)) {
	        Sheet sheet = workbook.getSheetAt(0);
	        Iterator<Row> rowIterator = sheet.iterator();

	        // Lấy header
	        List<String> headers = new ArrayList<>();
	        if (rowIterator.hasNext()) {
	            Row headerRow = rowIterator.next();
	            for (Cell cell : headerRow) {
	                headers.add(getCellStringValue(cell));
	            }
	            headers.add("Error"); // thêm cột lỗi
	        }

	        int rowNum = 1;
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	            rowNum++;

	            SwiftCodeDTO scd = new SwiftCodeDTO();
	            boolean hasError = false;
	            StringBuilder errorMessage = new StringBuilder();

	            scd.setBANK_TYPE(getCellStringValue(row.getCell(0)));
	            if (scd.getBANK_TYPE() == null || scd.getBANK_TYPE().isEmpty()) {
	                errorMessage.append("Loại ngân hàng không được để trống; ");
	                hasError = true;
	            }

	            scd.setSWIFT_CODE(getCellStringValue(row.getCell(1)));
	            if (scd.getSWIFT_CODE() == null || scd.getSWIFT_CODE().isEmpty()) {
	                errorMessage.append("Mã SWIFT không được để trống; ");
	                hasError = true;
	            } else if (scd.getSWIFT_CODE().length() < 8 || scd.getSWIFT_CODE().length() > 11) {
	                errorMessage.append("Mã SWIFT phải có độ dài 8-11 ký tự; ");
	                hasError = true;
	            }

	            scd.setBANK_NAME(getCellStringValue(row.getCell(4)));
	            if (scd.getBANK_NAME() == null || scd.getBANK_NAME().isEmpty()) {
	                errorMessage.append("Tên ngân hàng không được để trống; ");
	                hasError = true;
	            }

	            scd.setCOUNTRY_CODE(getCellStringValue(row.getCell(11)));
	            if (scd.getCOUNTRY_CODE() == null || scd.getCOUNTRY_CODE().isEmpty()
	                    || scd.getCOUNTRY_CODE().length() != 2) {
	                errorMessage.append("Mã quốc gia phải có 2 ký tự; ");
	                hasError = true;
	            }
	            
	            scd.setCONNECTED_TO_SWIFT(getCellStringValue(row.getCell(2)));
	            scd.setSWIFT_CONNECTION(getCellStringValue(row.getCell(3)));
	            scd.setBRANCH(getCellStringValue(row.getCell(5)));
	            scd.setADDRESS_1(getCellStringValue(row.getCell(6)));
	            scd.setADDRESS_2(getCellStringValue(row.getCell(7)));
	            scd.setADDRESS_3(getCellStringValue(row.getCell(8)));
	            scd.setADDRESS_4(getCellStringValue(row.getCell(9)));
	            scd.setCITY(getCellStringValue(row.getCell(10)));
	            String paraStatus = getCellStringValue(row.getCell(12));
	            scd.setPARA_STATUS("Active".equalsIgnoreCase(paraStatus) ? 1 : 0);
	            String activeStatus = getCellStringValue(row.getCell(13));
	            scd.setACTIVE_STATUS("Active".equalsIgnoreCase(activeStatus) ? 1 : 0);
	            scd.setJSON_DATA(getCellStringValue(row.getCell(14)));

	            // check trùng
	            if (!hasError) {
	                Optional<SwiftCodeEntity> existing = repository.findBySWIFTCODE(scd.getSWIFT_CODE());
	                if (existing.isPresent()) {
	                    errorMessage.append("Mã SWIFT đã tồn tại: ").append(scd.getSWIFT_CODE()).append("; ");
	                    hasError = true;
	                }
	            }

	            // Nếu có lỗi → ghi lại cả dòng
	            if (hasError) {
	                List<String> rowData = new ArrayList<>();
	                for (int i = 0; i <= 14; i++) {
	                    rowData.add(getCellStringValue(row.getCell(i)));
	                }
	                rowData.add(errorMessage.toString());
	                errorRows.add(rowData);
	                skippedCount++;
	            } else {
	                validSwiftCodes.add(maptoEntity(scd));
	                insertedCount++;
	            }
	        }

	        // Lưu DB
	        if (!validSwiftCodes.isEmpty()) {
	            repository.saveAll(validSwiftCodes);
	        }

	        // Nếu có lỗi → tạo file Excel lỗi (dạng byte[])
	        byte[] errorFile = null;
	        if (!errorRows.isEmpty()) {
	            Workbook errorWb = new XSSFWorkbook();
	            Sheet errorSheet = errorWb.createSheet("Errors");

	            // ghi header
	            Row headerRow = errorSheet.createRow(0);
	            for (int i = 0; i < headers.size(); i++) {
	                headerRow.createCell(i).setCellValue(headers.get(i));
	            }

	            // ghi data
	            int r = 1;
	            for (List<String> rowData : errorRows) {
	                Row errRow = errorSheet.createRow(r++);
	                for (int i = 0; i < rowData.size(); i++) {
	                    errRow.createCell(i).setCellValue(rowData.get(i));
	                }
	            }

	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            errorWb.write(bos);
	            errorWb.close();
	            errorFile = bos.toByteArray();
	        }

	        response.put("success", errorRows.isEmpty());
	        response.put("insertedCount", insertedCount);
	        response.put("skippedCount", skippedCount);
	        response.put("errorFile", errorFile);

	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("errorMessage", "Lỗi xử lý file Excel: " + e.getMessage());
	    }

	    return response;
	}

	//cập nhật trạng thái para_status
	public void updateParaStatus(int id, int para_status) {
		try {
			repository.updateParaStatus(id, para_status);
		} catch (Exception e) {
			throw new RuntimeException("Lỗi cập nhật trạng thái para_status: " + e.getMessage());
		}
	}

	@Override
	public ByteArrayInputStream exportToErrorExcel(List<SwiftCodeEntity> swiftCodes, List<ExcelError> errors)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
