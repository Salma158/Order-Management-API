package com.qeema.order_management_api.repository;

import com.qeema.order_management_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersItemsRepository extends JpaRepository<Order, Long> {
}
