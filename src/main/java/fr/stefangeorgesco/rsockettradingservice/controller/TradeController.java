package fr.stefangeorgesco.rsockettradingservice.controller;

import fr.stefangeorgesco.rsockettradingservice.client.StockServiceClient;
import fr.stefangeorgesco.rsockettradingservice.dto.StockTickDto;
import fr.stefangeorgesco.rsockettradingservice.dto.StockTradeRequest;
import fr.stefangeorgesco.rsockettradingservice.dto.StockTradeResponse;
import fr.stefangeorgesco.rsockettradingservice.service.TradeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("stock")
public class TradeController {

    private final TradeService tradeService;
    private final StockServiceClient stockServiceClient;

    public TradeController(TradeService tradeService, StockServiceClient stockServiceClient) {
        this.tradeService = tradeService;
        this.stockServiceClient = stockServiceClient;
    }

    @GetMapping(value = "tick/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockTickDto> stockTicks() {
        return stockServiceClient.stockStream();
    }

    @PostMapping("trade")
    public Mono<ResponseEntity<StockTradeResponse>> trade(@RequestBody Mono<StockTradeRequest> tradeRequestMono) {
        return tradeRequestMono
                .filter(tradeRequest -> tradeRequest.quantity() > 0)
                .flatMap(tradeService::trade)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
