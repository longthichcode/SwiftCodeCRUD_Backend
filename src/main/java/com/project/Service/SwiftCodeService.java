package com.project.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.DTO.SwiftCodeDTO;
import com.project.Entity.SwiftCodeEntity;
/**
 * bao gồm các phương thức :
 * tìm theo id
 * tìm theo điều kiện đặc biệt và trả về danh sách nếu các trường rỗng
 * thêm 
 * sửa 
 * xoá
 * phân trang
 */
public interface SwiftCodeService {
	SwiftCodeDTO findByID(int id) ;
	List<SwiftCodeDTO> findBySpe(SwiftCodeEntity sce);
	SwiftCodeDTO add(SwiftCodeDTO scd);
	SwiftCodeDTO update(int id ,SwiftCodeDTO scd);
	void delete(SwiftCodeEntity sce);
	Page<SwiftCodeDTO> paging(SwiftCodeEntity sf,Pageable page);
	ByteArrayInputStream exportToExcel(List<SwiftCodeEntity> swiftCodes) throws IOException;
	Map<String, Object> importFromExcel(InputStream inp) throws IOException;
}
