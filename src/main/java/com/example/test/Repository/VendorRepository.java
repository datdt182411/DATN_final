package com.example.test.Repository;

import com.example.test.Entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
}