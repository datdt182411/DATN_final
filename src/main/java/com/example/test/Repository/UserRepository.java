package com.example.test.Repository;

import com.example.test.Entity.AuthenticationType;
import com.example.test.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users getUsersByUsername(String name);

    Users getUsersByAuthenticationType(String authenticationType);
    Users getUsersById(Integer id);
//    public Users findByEmail(String email);
    public Users findByVerificationCode(String code);

    @Query("UPDATE Users  u SET u.authenticationType = ?2 WHERE u.id = ?1 ")
    @Modifying
    public void updateAuthenticationType(Integer userId , AuthenticationType type);

    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    public Users findByEmail(String email);

    public Users findByResetPasswordToken(String token);

    Users findByUsername(String userName);


}
