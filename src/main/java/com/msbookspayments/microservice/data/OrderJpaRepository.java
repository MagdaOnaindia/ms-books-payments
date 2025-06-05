package com.msbookspayments.microservice.data;

import com.msbookspayments.microservice.data.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
