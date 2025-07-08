package com.project.Controller;

import com.project.DTO.SwiftCodeDTO;
import com.project.DTO.SwiftCodeSearchDTO;
import com.project.Entity.SwiftCodeEntity;
import com.project.Service.SwiftCodeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jpadynamic/swiftcode")
@CrossOrigin(origins = "http://localhost:4200/")
public class SwiftCodeControllerJD {

    public final SwiftCodeService service;

    public SwiftCodeControllerJD(@Qualifier("JDService") SwiftCodeService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<SwiftCodeDTO>> findBySpecification(@RequestParam(defaultValue = "0") int page_number, @RequestParam(defaultValue = "30") int page_size) {
        Pageable pr = PageRequest.of(page_number, page_size);
        SwiftCodeEntity sce = new SwiftCodeEntity();
        Page<SwiftCodeDTO> page = this.service.paging(sce, pr);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SwiftCodeDTO> findById(@PathVariable int id) {
        SwiftCodeDTO scd = this.service.findByID(id);
        if (scd != null) {
            return new ResponseEntity<>(scd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SwiftCodeDTO> add(@RequestBody SwiftCodeDTO scd) {
        if (scd == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            this.service.add(scd);
            return new ResponseEntity<>(scd, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SwiftCodeDTO> update(@PathVariable int id, @RequestBody SwiftCodeDTO scd) {
        if (scd == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            this.service.update(id, scd);
            return new ResponseEntity<>(scd, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        SwiftCodeEntity sce = new SwiftCodeEntity();
        sce.setID(id);
        this.service.delete(sce);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}