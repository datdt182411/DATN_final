package com.example.test.Repository;

import com.example.test.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    List<Role> findRoleByName(String name);

}
