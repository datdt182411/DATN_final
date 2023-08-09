//package com.example.test.Repository;
//
//import com.example.test.Entity.Maintenance;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface MaintenanceRepository extends JpaRepository<Maintenance,Integer> {
//
//    @Override
//    List<Maintenance> findAllById(Iterable<Integer> iterable);
//
//    @Override
//    List<Maintenance> findAll();
//
//
////    @Query(value = "SELECT o.id " +
////            "FROM OrderLine  o " +
////            "JOIN o.product p " +
////            "JOIN p.maintenanceList m " +
////            "WHERE o.id = :order_lineId")
////    List<Object[]> findMaintenanceByOrderLineId(@Param("order_lineId") Integer orderLineId);
//
//    @Query(value = "SELECT od.id " +
//            "FROM Maintenance  m " +
//            "JOIN Product p ON m.product.id  = p.id " +
//            "JOIN OrderLine od ON p.id = od.product.id " +
//            "WHERE m.id = :maintenanceId")
//    String findOrderCodeByMaintenanceId(@Param("maintenanceId") Integer maintenanceId);
//
//
//    @Query(value = "SELECT * from  maintenance where maintenance.end_date BETWEEN ?1 AND ?2", nativeQuery = true)
//    List<Maintenance> maintenance_by_time(String date1, String date2);
//
//
//}
