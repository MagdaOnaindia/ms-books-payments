package com.msbookspayments.microservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msbookspayments.microservice.controller.model.OrderRequest;
import com.msbookspayments.microservice.data.model.Order;
import com.msbookspayments.microservice.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Gestión de órdenes de libros")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    // Endpoint para crear una nueva orden
    @PostMapping
    @Operation(
            operationId = "Insertar una orden",
            description = "Operacion de escritura",
            summary = "Creamos una orden a partir de sus datos",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        Order order = ordersService.createOrder(request);
        if (order == null) {
            return ResponseEntity.badRequest().body("Uno o más libros no son válidos o están ocultos.");
        }
        return ResponseEntity.ok(order);
    }

    // Obtener una orden por ID
    @GetMapping("/{id}")
    @Operation(
    		operationId = "Encuentra una Order  a través de si ID.",
            description = "Operacion de lectura",
    		summary = "Obtener una orden por ID")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No existe ninguna orden para ese Id.")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        Order order = ordersService.getOrder(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    // Listar todas las órdenes
    @GetMapping
    @Operation(
    		operationId = "Obtener listado de Orders",
            description = "Operacion de lectura",
    		summary = "Listar todas las órdenes")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = ordersService.getOrders();
        if (orders == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }
}