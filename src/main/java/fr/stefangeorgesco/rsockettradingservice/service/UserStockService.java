package fr.stefangeorgesco.rsockettradingservice.service;

import fr.stefangeorgesco.rsockettradingservice.dto.StockTradeRequest;
import fr.stefangeorgesco.rsockettradingservice.entity.UserStock;
import fr.stefangeorgesco.rsockettradingservice.repository.UserStockRepository;
import fr.stefangeorgesco.rsockettradingservice.util.EntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserStockService {

    private final UserStockRepository stockRepository;

    public UserStockService(UserStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Mono<UserStock> buyStock(StockTradeRequest request) {
        return stockRepository.findByUserIdAndStockSymbol(request.userId(), request.stockSymbol())
                .defaultIfEmpty(EntityDtoUtil.toUserStock(request))
                .doOnNext(userStock -> userStock.setQuantity(userStock.getQuantity() + request.quantity()))
                .flatMap(stockRepository::save);
    }

    public Mono<UserStock> sellStock(StockTradeRequest request) {
        return stockRepository.findByUserIdAndStockSymbol(request.userId(), request.stockSymbol())
                .filter(userStock -> userStock.getQuantity() >= request.quantity())
                .doOnNext(userStock -> userStock.setQuantity(userStock.getQuantity() - request.quantity()))
                .flatMap(stockRepository::save);
    }
}
