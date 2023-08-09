package com.example.test.Repository;

import com.example.test.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Integer> {
    List<Type> findAllByNameContaining(String keyword);

    public  boolean existsByName(String name);
}
