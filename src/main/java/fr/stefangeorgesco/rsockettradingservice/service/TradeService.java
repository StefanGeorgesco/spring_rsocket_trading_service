package fr.stefangeorgesco.rsockettradingservice.service;

import fr.stefangeorgesco.rsockettradingservice.client.StockServiceClient;
import fr.stefangeorgesco.rsockettradingservice.client.UserServiceClient;
import fr.stefangeorgesco.rsockettradingservice.domain.TradeStatus;
import fr.stefangeorgesco.rsockettradingservice.domain.TransactionStatus;
import fr.stefangeorgesco.rsockettradingservice.dto.StockTradeRequest;
import fr.stefangeorgesco.rsockettradingservice.dto.StockTradeResponse;
import fr.stefangeorgesco.rsockettradingservice.dto.TransactionRequest;
import fr.stefangeorgesco.rsockettradingservice.util.EntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TradeService {

    private final UserStockService userStockService;
    private final UserServiceClient userServiceClient;
    private final StockServiceClient stockServiceClient;

    public TradeService(UserStockService userStockService, UserServiceClient userServiceClient, StockServiceClient stockServiceClient) {
        this.userStockService = userStockService;
        this.userServiceClient = userServiceClient;
        this.stockServiceClient = stockServiceClient;
    }

    public Mono<StockTradeResponse> trade(StockTradeRequest tradeRequest) {
        TransactionRequest transactionRequest =
                EntityDtoUtil.toTransactionRequest(tradeRequest, estimatePrice(tradeRequest));
        Mono<StockTradeResponse> response = switch (tradeRequest.tradeType()) {
            case BUY -> buyStock(tradeRequest, transactionRequest);
            case SELL -> sellStock(tradeRequest, transactionRequest);
        };
        return response
                .defaultIfEmpty(EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.FAILED, 0));
    }

    private Mono<StockTradeResponse> buyStock(StockTradeRequest tradeRequest, TransactionRequest transactionRequest) {
        return userServiceClient.doTransaction(transactionRequest)
                .filter(transactionResponse ->
                        TransactionStatus.COMPLETED.equals(transactionResponse.status()))
                .flatMap(transactionResponse -> userStockService.buyStock(tradeRequest))
                .map(userStock -> EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.COMPLETED,
                        transactionRequest.amount()));
    }

    private Mono<StockTradeResponse> sellStock(StockTradeRequest tradeRequest, TransactionRequest transactionRequest) {
        return userStockService.sellStock(tradeRequest)
                .flatMap(userStock -> userServiceClient.doTransaction(transactionRequest))
                .map(transactionResponse ->
                        EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.COMPLETED,
                                transactionRequest.amount()));

    }

    private int estimatePrice(StockTradeRequest request) {
        return request.quantity() * stockServiceClient.getCurrentStockPrice(request.stockSymbol());
    }
}
