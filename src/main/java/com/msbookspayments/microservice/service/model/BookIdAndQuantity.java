package com.msbookspayments.microservice.service.model;


import lombok.Getter;

@Getter
public class BookIdAndQuantity {
    private final Long numericId;
    private final int quantity;

    public BookIdAndQuantity(Long numericId, int quantity) {
        this.numericId = numericId;
        this.quantity = quantity;
    }
}