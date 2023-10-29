package com.Cinema.Cinema.repository;

import com.Cinema.Cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("Select u From User u Where u.email=?1")
    public User findByEmail(@Param ("email") String email);
    @Query("Update User u Set u.enabled = true Where u.id = ?1")
    @Modifying
    public void enable(Long id);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);
    public User findByResetPasswordToken(String token);

}
