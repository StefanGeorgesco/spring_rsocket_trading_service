package fr.stefangeorgesco.rsockettradingservice.client;

import fr.stefangeorgesco.rsockettradingservice.dto.TransactionRequest;
import fr.stefangeorgesco.rsockettradingservice.dto.TransactionResponse;
import fr.stefangeorgesco.rsockettradingservice.dto.UserDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {

    private final RSocketRequester requester;

    public UserServiceClient(RSocketRequester.Builder builder,
                             RSocketConnectorConfigurer connectorConfigurer,
                             @Value("${service.user.host}") String userServiceHost,
                             @Value("${service.user.port}") int userServicePort) {
        this.requester = builder
                .rsocketConnector(connectorConfigurer)
                .transport(TcpClientTransport.create(userServiceHost, userServicePort));
    }

    public Mono<TransactionResponse> doTransaction(TransactionRequest transactionRequest) {
        return requester
                .route("user.transaction")
                .data(transactionRequest)
                .retrieveMono(TransactionResponse.class)
                .doOnNext(System.out::println);
    }

    public Flux<UserDto> getAllUsers() {
        return requester
                .route("user.get.all")
                .retrieveFlux(UserDto.class);
    }
}
