package com.msbookspayments.microservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.msbookspayments.microservice.controller.model.OrderItemRequest;
import com.msbookspayments.microservice.controller.model.OrderItemResponse;
import com.msbookspayments.microservice.controller.model.OrderResponse;
import com.msbookspayments.microservice.data.model.OrderItem;
import com.msbookspayments.microservice.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import static com.msbookspayments.microservice.mapper.OrderMapper.toOrderResponse;

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
    @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes, ID de libro no válido, cantidad no positiva)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "403", description = "Libro no disponible para la venta (ej. no visible)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de stock (stock insuficiente)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        try {
            Order createdOrderEntity = ordersService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(toOrderResponse(createdOrderEntity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((OrderResponse) Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("no está disponible")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body((OrderResponse) Map.of("error", e.getMessage()));
            } else if (e.getMessage().contains("Stock insuficiente")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body((OrderResponse) Map.of("error", e.getMessage()));
            }
            return ResponseEntity.badRequest().body((OrderResponse) Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((OrderResponse) Map.of("error", "Error procesando la orden: " + e.getMessage()));
        }
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
        return ResponseEntity.ok(toOrderResponse(order));
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
        return ResponseEntity.ok(orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList()));
    }
}