package com.project.Controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.DTO.SwiftCodeDTO;
import com.project.DTO.SwiftCodeSearchDTO;
import com.project.Entity.SwiftCodeEntity;
import com.project.Service.SwiftCodeService;

/**
 * Bộ điều khiển REST cho các thao tác liên quan đến mã Swift.
 */
@RestController
@RequestMapping("/api/entitymanager/swiftcode")
public class SwiftCodeControllerEM {

    // Dịch vụ xử lý logic nghiệp vụ cho mã Swift
    private final SwiftCodeService service;

    /**
     * Constructor để tiêm dịch vụ SwiftCodeService.
     * @param service Dịch vụ mã Swift được tiêm với định danh "EMService".
     */
    public SwiftCodeControllerEM(@Qualifier("EMService") SwiftCodeService service) {
        this.service = service;
    }

    /**
     * Tìm kiếm mã Swift theo các tiêu chí được chỉ định và trả về kết quả phân trang.
     * @param searchDTO Đối tượng chứa các tiêu chí tìm kiếm (BANK_TYPE, BANK_NAME, BRANCH, CITY, COUNTRY_CODE, page_number, page_size).
     * @return ResponseEntity chứa danh sách phân trang các SwiftCodeDTO nếu thành công, mã trạng thái HTTP 200 (OK).
     */
    @PostMapping("/search")
    public ResponseEntity<Page<SwiftCodeDTO>> findBySpecification(@RequestBody SwiftCodeSearchDTO searchDTO) {
        SwiftCodeEntity sce = new SwiftCodeEntity();
        sce.setBANK_TYPE(searchDTO.getBANK_TYPE());
        sce.setBANK_NAME(searchDTO.getBANK_NAME());
        sce.setBRANCH(searchDTO.getBRANCH());
        sce.setCITY(searchDTO.getCITY());
        sce.setCOUNTRY_CODE(searchDTO.getCOUNTRY_CODE());

        Pageable pr = PageRequest.of(searchDTO.getPage_number(), searchDTO.getPage_size());
        Page<SwiftCodeDTO> page = this.service.paging(sce, pr);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * Tìm mã Swift theo ID.
     * @param id ID của mã Swift cần tìm.
     * @return ResponseEntity chứa SwiftCodeDTO nếu tìm thấy (HTTP 200 OK), hoặc HTTP 404 (NOT_FOUND) nếu không tìm thấy.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SwiftCodeDTO> findById(@PathVariable int id) {
        SwiftCodeDTO scd = this.service.findByID(id);
        if (scd != null) {
            return new ResponseEntity<>(scd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Thêm một mã Swift mới.
     * @param scd Đối tượng SwiftCodeDTO chứa thông tin mã Swift cần thêm.
     * @return ResponseEntity chứa SwiftCodeDTO nếu thêm thành công (HTTP 200 OK), hoặc HTTP 400 (BAD_REQUEST) nếu dữ liệu đầu vào không hợp lệ.
     */
    @PostMapping
    public ResponseEntity<SwiftCodeDTO> add(@RequestBody SwiftCodeDTO scd) {
        if (scd == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            this.service.add(scd);
            return new ResponseEntity<>(scd, HttpStatus.OK);
        }
    }

    /**
     * Cập nhật thông tin mã Swift theo ID.
     * @param id ID của mã Swift cần cập nhật.
     * @param scd Đối tượng SwiftCodeDTO chứa thông tin mới để cập nhật.
     * @return ResponseEntity chứa SwiftCodeDTO nếu cập nhật thành công (HTTP 200 OK), hoặc HTTP 400 (BAD_REQUEST) nếu dữ liệu đầu vào không hợp lệ.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SwiftCodeDTO> update(@PathVariable int id, @RequestBody SwiftCodeDTO scd) {
        if (scd == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            this.service.update(id, scd);
            return new ResponseEntity<>(scd, HttpStatus.OK);
        }
    }

    /**
     * Xóa mã Swift theo ID.
     * @param id ID của mã Swift cần xóa.
     * @return ResponseEntity không chứa nội dung (void) nếu xóa thành công.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        SwiftCodeEntity sce = new SwiftCodeEntity();
        sce.setID(id);
        this.service.delete(sce);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}