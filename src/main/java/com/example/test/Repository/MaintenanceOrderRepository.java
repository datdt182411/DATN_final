package com.example.test.Repository;


import com.example.test.Entity.MaintenanceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, Integer> {
    @Override
    List<MaintenanceOrder> findAllById(Iterable<Integer> iterable);

    @Override
    List<MaintenanceOrder> findAll();

    @Query(value = "SELECT * from  maintenanceOrder where maintenanceOrder.end_date BETWEEN ?1 AND ?2", nativeQuery = true)
    List<MaintenanceOrder> maintenance_by_time(String date1, String date2);


}
