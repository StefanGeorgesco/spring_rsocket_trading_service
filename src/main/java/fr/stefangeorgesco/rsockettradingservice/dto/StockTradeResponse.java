package fr.stefangeorgesco.rsockettradingservice.dto;

import fr.stefangeorgesco.rsockettradingservice.domain.TradeStatus;
import fr.stefangeorgesco.rsockettradingservice.domain.TradeType;

public record StockTradeResponse(String userId,
                                 String stockSymbol,
                                 int quantity,
                                 TradeType tradeType,
                                 TradeStatus tradeStatus,
                                 int price) {
}
