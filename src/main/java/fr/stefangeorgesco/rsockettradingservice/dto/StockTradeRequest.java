package fr.stefangeorgesco.rsockettradingservice.dto;

import fr.stefangeorgesco.rsockettradingservice.domain.TradeType;

public record StockTradeRequest(String userId,
                                String stockSymbol,
                                int quantity,
                                TradeType tradeType) {
}
