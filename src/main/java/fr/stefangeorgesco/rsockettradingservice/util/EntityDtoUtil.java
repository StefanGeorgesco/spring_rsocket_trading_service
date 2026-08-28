package fr.stefangeorgesco.rsockettradingservice.util;

import fr.stefangeorgesco.rsockettradingservice.domain.TradeStatus;
import fr.stefangeorgesco.rsockettradingservice.domain.TradeType;
import fr.stefangeorgesco.rsockettradingservice.domain.TransactionType;
import fr.stefangeorgesco.rsockettradingservice.dto.StockTradeRequest;
import fr.stefangeorgesco.rsockettradingservice.dto.StockTradeResponse;
import fr.stefangeorgesco.rsockettradingservice.dto.TransactionRequest;
import fr.stefangeorgesco.rsockettradingservice.entity.UserStock;

public class EntityDtoUtil {

    private EntityDtoUtil() {
    }

    public static TransactionRequest toTransactionRequest(StockTradeRequest stockTradeRequest, int amount) {
        return new TransactionRequest(stockTradeRequest.userId(), toTransactionType(stockTradeRequest.tradeType()),
                amount);
    }

    public static StockTradeResponse toTradeResponse(StockTradeRequest stockTradeRequest, TradeStatus tradeStatus,
                                                     int price) {
        return new StockTradeResponse(stockTradeRequest.userId(), stockTradeRequest.stockSymbol(),
                stockTradeRequest.quantity(), stockTradeRequest.tradeType(), tradeStatus, price);
    }

    public static UserStock toUserStock(StockTradeRequest stockTradeRequest) {
        UserStock stock = new UserStock();
        stock.setUserId(stockTradeRequest.userId());
        stock.setStockSymbol(stockTradeRequest.stockSymbol());
        stock.setQuantity(0);
        return stock;
    }

    private static TransactionType toTransactionType(TradeType tradeType) {
        return switch (tradeType) {
            case BUY -> TransactionType.DEBIT;
            case SELL -> TransactionType.CREDIT;
        };
    }
}
