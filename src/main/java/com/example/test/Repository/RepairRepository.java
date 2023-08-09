package com.example.test.Repository;

import com.example.test.Entity.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRepository extends JpaRepository<Repair,Integer> {
    Repair getRepairById(Integer id);
}
