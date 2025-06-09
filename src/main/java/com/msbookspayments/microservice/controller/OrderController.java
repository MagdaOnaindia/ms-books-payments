package com.msbookspayments.microservice.controller;

import com.msbookspayments.microservice.controller.model.OrderRequest;
import com.msbookspayments.microservice.data.model.Order;
import com.msbookspayments.microservice.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Gestión de órdenes de libros")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    // Endpoint para crear una nueva orden
    @PostMapping
    @Operation(summary = "Crear una nueva orden")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        Order order = ordersService.createOrder(request);
        if (order == null) {
            return ResponseEntity.badRequest().body("Uno o más libros no son válidos o están ocultos.");
        }
        return ResponseEntity.ok(order);
    }

    // Obtener una orden por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una orden por ID")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        Order order = ordersService.getOrder(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    // Listar todas las órdenes
    @GetMapping
    @Operation(summary = "Listar todas las órdenes")
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = ordersService.getOrders();
        if (orders == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }
}