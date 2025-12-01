package com.project.Security.Repository;

import com.project.Security.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    // Tìm người dùng theo tên đăng nhập
    @Query(value = "SELECT * FROM SC_USERS WHERE USER_NAME = :username", nativeQuery = true)
    Optional<UserEntity> findByUsername(@Param("username") String username);

    //kiểm tra xem người dùng đã tồn tại chưa
    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM SC_USERS WHERE USER_NAME = :username) THEN 1 ELSE 0 END FROM DUAL", nativeQuery = true)
    int existsByUSER_NAME(@Param("username") String username);

    // Chỉnh sửa vai trò của người dùng
    @Modifying
    @Query(value = "DELETE FROM SC_USER_ROLE WHERE USER_ID = :userId; " +
                   "INSERT INTO SC_USER_ROLE (USER_ID, ROLE_ID) " +
                   "SELECT :userId, r.ID FROM SC_ROLES r WHERE r.ROLE_NAME = :roleName", 
           nativeQuery = true)
    void editRole(@Param("userId") Integer userId, @Param("roleName") String roleName);
    // Chỉnh sửa trạng thai của người dùng
    @Modifying
    @Query(value = "UPDATE SC_USERS SET ENABLED = :status WHERE ID = :userId", nativeQuery = true)
    void editStatus(@Param("userId") Integer userId, @Param("status") int yn);
    
    // Lấy tất cả người dùng
    @Query(value = "SELECT * FROM SC_USERS", nativeQuery = true)
    List<UserEntity> findAllUsers();
    
    //tìm vai trò của người dùnng
    @Query(value = "SELECT r.ROLE_NAME FROM SC_USER_ROLE ur " +
				   "JOIN SC_ROLES r ON ur.ROLE_ID = r.ID " +
				   "WHERE ur.USER_ID = :userId", nativeQuery = true)
    List<String> findRolesByUserId(@Param("userId") Integer userId);
    
    // xoá thông tin về người dùng 
    @Modifying
    @Query(value = "DELETE FROM SC_USER_ROLE WHERE USER_ID = :userId", nativeQuery = true)
    void deleteUserRoleByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query(value = "DELETE FROM SC_USERS WHERE ID = :userId", nativeQuery = true)
    void deleteUserById(@Param("userId") Integer userId);
    
}