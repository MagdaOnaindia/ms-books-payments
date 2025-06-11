package com.msbookspayments.microservice.mapper;

import com.msbookspayments.microservice.controller.model.OrderItemResponse;
import com.msbookspayments.microservice.controller.model.OrderResponse;
import com.msbookspayments.microservice.data.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderResponse toOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setOrderDate(order.getOrderDate());

        if (order.getItems() != null) {
            List<OrderItemResponse> itemResponses = order.getItems().stream()
                    .map(itemEntity -> {
                        OrderItemResponse itemDto = new OrderItemResponse();
                        itemDto.setBookId(itemEntity.getBookId());
                        itemDto.setQuantity(itemEntity.getQuantity());
                        return itemDto;
                    })
                    .collect(Collectors.toList());
            orderResponse.setItems(itemResponses);
        } else {
            orderResponse.setItems(new ArrayList<>());
        }
        return orderResponse;
    }
}
