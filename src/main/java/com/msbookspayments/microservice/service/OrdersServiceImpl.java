package com.msbookspayments.microservice.service;

import com.msbookspayments.microservice.controller.model.OrderItemRequest;
import com.msbookspayments.microservice.data.OrderJpaRepository;
import com.msbookspayments.microservice.data.model.Order;
import com.msbookspayments.microservice.data.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.msbookspayments.microservice.facade.BooksFacade;
import com.msbookspayments.microservice.facade.model.Book;
import com.msbookspayments.microservice.controller.model.OrderRequest;

@Service
public class OrdersServiceImpl implements OrdersService {

  @Autowired
  private BooksFacade booksFacade;

  @Autowired
  private OrderJpaRepository repository;

  @Override
  public Order createOrder(OrderRequest request) {
    if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
      throw new IllegalArgumentException("La solicitud de orden es inválida (vacía o sin ítems).");
    }

    Order newOrder = Order.builder()
            .orderDate(LocalDateTime.now())
            .items(new ArrayList<>())
            .build();

    // iteramos sobre los items de la orden
    for (OrderItemRequest itemReq : request.getItems()) {
      if (itemReq.getQuantity() <= 0) {
        throw new IllegalArgumentException("La cantidad para el libro ID " + itemReq.getBookId() + " debe ser positiva.");
      }

      Long bookIdLong;
      try {
        bookIdLong = Long.valueOf(itemReq.getBookId());
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("El ID del libro '" + itemReq.getBookId() + "' no es un número válido.");
      }

      Book bookFromCatalogue = booksFacade.getBook(itemReq.getBookId());

      if (bookFromCatalogue == null) {
        throw new IllegalArgumentException("Libro con ID " + itemReq.getBookId() + " no encontrado en el catálogo.");
      }
      if (Boolean.FALSE.equals(bookFromCatalogue.getVisible())) { // Comprobar explícitamente false
        throw new IllegalStateException("Libro '" + bookFromCatalogue.getTitulo() + "' (ID: " + itemReq.getBookId() + ") no está disponible.");
      }
      //solo comprobamos stock, no actualizamos por ahora
      if (bookFromCatalogue.getStock() == null || bookFromCatalogue.getStock() < itemReq.getQuantity()) {
        throw new IllegalStateException("Stock insuficiente para el libro '" + bookFromCatalogue.getTitulo() + "' (ID: " + itemReq.getBookId() + "). Solicitado: " + itemReq.getQuantity() + ", Disponible: " + (bookFromCatalogue.getStock() == null ? 0 : bookFromCatalogue.getStock()));
      }

      OrderItem orderItem = OrderItem.builder()
              .bookId(bookIdLong)
              .quantity(itemReq.getQuantity())
              .order(newOrder)
              .build();
      newOrder.addItem(orderItem);
    }

    return repository.save(newOrder);
  }

  @Override
  public Order getOrder(String id) {
    return repository.findById(Long.valueOf(id)).orElse(null);
  }

  @Override
  public List<Order> getOrders() {
    List<Order> orders = repository.findAll();
    return orders.isEmpty() ? null : orders;
  }
}
