package fr.stefangeorgesco.rsockettradingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class RSocketClientConfig {

    @Bean
    public RSocketConnectorConfigurer connectorConfigurer() {
        return rSocketConnector -> rSocketConnector.reconnect(retryStrategy());
    }

    private Retry retryStrategy() {
        return Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(2))
                .doBeforeRetry(retrySignal ->
                        System.out.println("Retrying connection... Attempt: " +
                                (retrySignal.totalRetriesInARow() + 1)));
    }
}
