package com.project.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.project.DTO.SwiftCodeDTO;
import com.project.Entity.SwiftCodeEntity;

import oracle.jdbc.OracleTypes;

@Component
public class SwiftCodeProcedureCaller {

	@PersistenceContext
    private final EntityManager entityManager;

    public SwiftCodeProcedureCaller(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<SwiftCodeDTO> callSearchProcedure(SwiftCodeEntity sce, int pageNumber, int pageSize) {
        Connection connection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<SwiftCodeDTO> resultList = new ArrayList<>();

        try {
            // Lấy connection từ EntityManager
            connection = entityManager.unwrap(Connection.class);
            // Chuẩn bị gọi stored procedure
            String callStmt = "{call sp_search_swift_code(?,?,?,?,?,?,?,?)}";
            stmt = connection.prepareCall(callStmt);

            // Thiết lập các tham số đầu vào
            stmt.setString(1, sce.getBANK_TYPE());
            stmt.setString(2, sce.getBANK_NAME());
            stmt.setString(3, sce.getBRANCH());
            stmt.setString(4, sce.getCITY());
            stmt.setString(5, sce.getCOUNTRY_CODE());
            
            // Đăng ký tham số đầu ra
            stmt.registerOutParameter(9, OracleTypes.CURSOR); // Cursor kết quả

            // Thực thi stored procedure
            stmt.execute();

            // Lấy cursor và chuyển đổi thành danh sách SwiftCodeDTO
            rs = (ResultSet) stmt.getObject(9);
            
            while (rs.next()) {
                SwiftCodeDTO dto = new SwiftCodeDTO(
                    rs.getInt("ID"),
                    rs.getString("BANK_TYPE"),
                    rs.getString("SWIFT_CODE"),
                    rs.getString("CONNECTED_TO_SWIFT"),
                    rs.getString("SWIFT_CONNECTION"),
                    rs.getString("BANK_NAME"),
                    rs.getString("BRANCH"),
                    rs.getString("ADDRESS_1"),
                    rs.getString("ADDRESS_2"),
                    rs.getString("ADDRESS_3"),
                    rs.getString("ADDRESS_4"),
                    rs.getString("CITY"),
                    rs.getString("COUNTRY_CODE"),
                    rs.getInt("PARA_STATUS"),
                    rs.getInt("ACTIVE_STATUS"),
                    rs.getString("JSON_DATA")
                );
                resultList.add(dto);
            }

            return resultList;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi stored procedure: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                throw new RuntimeException("Lỗi khi đóng tài nguyên: " + e.getMessage(), e);
            }
        }
    }
}