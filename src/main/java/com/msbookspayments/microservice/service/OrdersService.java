package com.msbookspayments.microservice.service;

import com.msbookspayments.microservice.data.model.Order;
import com.msbookspayments.microservice.controller.model.OrderRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrdersService {
	
	Order createOrder(OrderRequest request);

	Order getOrder(String id);

	List<Order> getOrders();

}