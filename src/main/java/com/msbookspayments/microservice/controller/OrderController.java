package com.msbookspayments.microservice.controller;

import com.msbookspayments.microservice.controller.model.OrderRequest;
import com.msbookspayments.microservice.data.model.Order;
import com.msbookspayments.microservice.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    // Endpoint para crear una nueva orden
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        Order order = ordersService.createOrder(request);
        if (order == null) {
            return ResponseEntity.badRequest().body("Uno o más libros no son válidos o están ocultos.");
        }
        return ResponseEntity.ok(order);
    }

    // Obtener una orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        Order order = ordersService.getOrder(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    // Listar todas las órdenes
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = ordersService.getOrders();
        if (orders == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }
}