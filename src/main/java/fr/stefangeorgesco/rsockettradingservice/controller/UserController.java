package fr.stefangeorgesco.rsockettradingservice.controller;

import fr.stefangeorgesco.rsockettradingservice.client.UserServiceClient;
import fr.stefangeorgesco.rsockettradingservice.dto.UserDto;
import fr.stefangeorgesco.rsockettradingservice.dto.UserStockDto;
import fr.stefangeorgesco.rsockettradingservice.service.UserStockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserServiceClient userServiceClient;
    private final UserStockService userStockService;

    public UserController(UserServiceClient userServiceClient, UserStockService userStockService) {
        this.userServiceClient = userServiceClient;
        this.userStockService = userStockService;
    }

    @GetMapping("all")
    public Flux<UserDto> getAllUsers() {
        return userServiceClient.getAllUsers();
    }

    @GetMapping("{userId}/stocks")
    public Flux<UserStockDto> getUserStocks(@PathVariable String userId) {
        return userStockService.getUserStocks(userId);
    }
}
