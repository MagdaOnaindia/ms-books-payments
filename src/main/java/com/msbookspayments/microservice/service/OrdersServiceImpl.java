package com.msbookspayments.microservice.service;

import com.msbookspayments.microservice.data.OrderJpaRepository;
import com.msbookspayments.microservice.data.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.msbookspayments.microservice.facade.BooksFacade;
import com.msbookspayments.microservice.facade.model.Book;
import com.msbookspayments.microservice.controller.model.OrderRequest;

@Service
public class OrdersServiceImpl implements OrdersService {

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private BooksFacade booksFacade;

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private OrderJpaRepository repository;

  @Override
  public Order createOrder(OrderRequest request) {

    List<Book> books = request.getBooks().stream().map(booksFacade::getProduct).filter(Objects::nonNull).toList();

    if(books.size() != request.getBooks().size() || books.stream().anyMatch(book -> !book.getVisible())) {
      return null;
    } else {
      Order order = Order.builder().books(books.stream().map(Book::getId).collect(Collectors.toList())).build();
      repository.save(order);
      return order;
    }
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
