package com.example.test.Repository;

import com.example.test.Entity.Customer;
import com.example.test.Entity.Order;
import com.example.test.Entity.OrderStatus;
import com.example.test.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderById(Integer id);

    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    Page<Order> findAllByStatus(int status, Pageable pageable);

    //    @Query(value = "EXEC bookedTicketForUser :accountID", nativeQuery = true)
//    List<Object> getList(@Param("accountID") Integer accountID);
//
//    @Query(value = "EXEC UserHistory ?1, ?2, ?3", nativeQuery = true)
//    List<Object> getHistory(Integer accountID, Date fromDate, Date toDate);
    @Query(value = "CALL manager_order(:manager_order);", nativeQuery = true)
    List<Object> managerOrder(@Param("manager_order") Integer year_in);

    @Query(value = "SELECT * from  orders where orders.order_date BETWEEN ?1 AND ?2" ,nativeQuery = true)
    List<Order> orders_by_time(String date1,String date2);

    @Query(value = "CALL manager_order_by_month(:years);" ,nativeQuery = true)
    List<Object> orders_by_month(@Param("years") Integer years);

    @Query ("SELECT NEW com.example.test.Entity.Order(o.id, o.orderConfirmDate, o.amount, o.status) FROM Order o WHERE o.orderConfirmDate BETWEEN ?1 and ?2 ORDER BY o.orderConfirmDate ASC")
    public List<Order> findByOrderDateBetween(Date startTime, Date endTime);


    public Order findByIdAndUser(Integer id, Users user);
}
