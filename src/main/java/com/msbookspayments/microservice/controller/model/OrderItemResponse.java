package com.msbookspayments.microservice.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    //así enseñamos los campos que queremos que se devuelvan al cliente
    private Long bookId;
    private Integer quantity;

}