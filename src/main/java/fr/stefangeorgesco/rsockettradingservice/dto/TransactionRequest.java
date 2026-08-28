package fr.stefangeorgesco.rsockettradingservice.dto;

import fr.stefangeorgesco.rsockettradingservice.domain.TransactionType;

public record TransactionRequest(String userId,
                                 TransactionType type,
                                 int amount) {
}
