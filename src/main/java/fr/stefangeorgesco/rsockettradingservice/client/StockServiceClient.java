package fr.stefangeorgesco.rsockettradingservice.client;

import fr.stefangeorgesco.rsockettradingservice.dto.StockTickDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockServiceClient {

    private final RSocketRequester requester;
    private final Map<String,Integer> stockPrices;
    private Flux<StockTickDto> stockTickFlux;

    public StockServiceClient(RSocketRequester.Builder builder,
                              RSocketConnectorConfigurer connectorConfigurer,
                              @Value("${service.stock.host}") String stockServiceHost,
                              @Value("${service.stock.port}") int stockServicePort) {
        this.requester = builder
                .rsocketConnector(connectorConfigurer)
                .transport(TcpClientTransport.create(stockServiceHost, stockServicePort));
        this.stockPrices = new ConcurrentHashMap<>();
        initialize();
    }

    public Flux<StockTickDto> stockStream() {
        return stockTickFlux;
    }

    public int getCurrentStockPrice(String stockSymbol) {
        return stockPrices.get(stockSymbol);
    }

    private void initialize() {
        stockTickFlux = requester
                .route("stock.ticks")
                .retrieveFlux(StockTickDto.class)
                .doOnNext(stockTick -> stockPrices.put(stockTick.code(), stockTick.price()))
                .retryWhen(retryStrategy())
                .publish()
                .autoConnect(0);
    }

    private Retry retryStrategy() {
        return Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(2))
                .doBeforeRetry(retrySignal ->
                        System.out.println("Retrying to connect to stock service... Attempt: " +
                                (retrySignal.totalRetriesInARow() + 1)));
    }
}
