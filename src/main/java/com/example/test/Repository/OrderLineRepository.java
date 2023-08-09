package com.example.test.Repository;

import com.example.test.Entity.OrderLine;
import com.example.test.Entity.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {

//    INSERT INTO `medical`.`order_line` (`order_id`, `product_id`, `quantity`) VALUES ('1', '1', '1');

        List<OrderLine> findByOrderId(Integer id);

        @Query(value = "select new com.example.test.Entity.OrderLine(d.product.type.name, d.quantity, d.product.price, d.order.status)" +
                " from OrderLine d WHERE d.order.orderConfirmDate between ?1 and ?2 ")
        public List<OrderLine> findWithTypeAndTimeBetween(Date startTime, Date endTime);

        @Query(value = "select new com.example.test.Entity.OrderLine(d.quantity, d.product.name, d.product.price, d.order.status, d.product.type.name)" +
                " from OrderLine d WHERE d.order.orderConfirmDate between ?1 and ?2 ")
        public List<OrderLine> findWithProductAndTimeBetween(Date startTime, Date endTime);


        @Query("SELECT COUNT(d) FROM OrderLine d JOIN Order o ON d.order.id = o.id"
                + " WHERE d.product.id = ?1 AND d.order.user.id = ?2 AND"
                + " o.orderStatus = ?3")
        public Long countByProductAndUserAndOrderStatus(
                Integer productId, Integer customerId, OrderStatus status);
}
