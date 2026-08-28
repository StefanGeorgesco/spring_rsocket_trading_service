package fr.stefangeorgesco.rsockettradingservice.dto;

public record UserStockDto(String id,
                           String userId,
                           String stockSymbol,
                           int quantity) {
}
