package com.project.Controller;

import com.project.DTO.ExcelError;
import com.project.DTO.SwiftCodeDTO;

import com.project.DTO.SwiftCodeSearchDTO;
import com.project.Entity.SwiftCodeEntity;
import com.project.Service.SwiftCodeIServiceJPADynamic;
import com.project.Service.SwiftCodeService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/jpadynamic/swiftcode")
@CrossOrigin(origins = "http://localhost:4200/")
public class SwiftCodeControllerJD {

    public final SwiftCodeService service;

    public SwiftCodeControllerJD(@Qualifier("JDService") SwiftCodeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<SwiftCodeDTO>> findBySpecification(@RequestParam(defaultValue = "0") int page_number, @RequestParam(defaultValue = "30") int page_size) {
        Pageable pr = PageRequest.of(page_number, page_size);
        SwiftCodeEntity sce = new SwiftCodeEntity();
        Page<SwiftCodeDTO> page = this.service.paging(sce, pr);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<SwiftCodeDTO> findById(@PathVariable int id) {
        SwiftCodeDTO scd = this.service.findByID(id);
        if (scd != null) {
            return new ResponseEntity<>(scd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<SwiftCodeDTO> add(@RequestBody SwiftCodeDTO scd) {
        if (scd == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            this.service.add(scd);
            return new ResponseEntity<>(scd, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SwiftCodeDTO> update(@PathVariable int id, @RequestBody SwiftCodeDTO scd) {
        if (scd == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            this.service.update(id, scd);
            return new ResponseEntity<>(scd, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        SwiftCodeEntity sce = new SwiftCodeEntity();
        sce.setID(id);
        this.service.delete(sce);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportSwiftCodesToExcel(@RequestBody SwiftCodeSearchDTO searchDTO) {
        try {
            SwiftCodeEntity sce = new SwiftCodeEntity();
            sce.setBANK_TYPE(searchDTO.getBANK_TYPE());
            sce.setBANK_NAME(searchDTO.getBANK_NAME());
            sce.setBRANCH(searchDTO.getBRANCH());
            sce.setCITY(searchDTO.getCITY());
            sce.setCOUNTRY_CODE(searchDTO.getCOUNTRY_CODE());

            // Lấy danh sách SwiftCode dựa trên tiêu chí tìm kiếm
            List<SwiftCodeDTO> swiftCodeDTOs = service.findBySpe(sce);
            List<SwiftCodeEntity> swiftCodes = swiftCodeDTOs.stream()
                    .map(SwiftCodeIServiceJPADynamic::maptoEntity)
                    .collect(Collectors.toList());

            // Xuất danh sách ra Excel
            ByteArrayInputStream in = service.exportToExcel(swiftCodes);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=swift_codes.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(in.readAllBytes());
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importSwiftCodesFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = service.importFromExcel(file.getInputStream());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("errors", List.of(new ExcelError(0, 0, "Lỗi đọc file Excel: " + e.getMessage())));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}