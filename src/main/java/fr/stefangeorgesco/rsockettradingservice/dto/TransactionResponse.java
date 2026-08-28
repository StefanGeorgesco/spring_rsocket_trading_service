package fr.stefangeorgesco.rsockettradingservice.dto;

import fr.stefangeorgesco.rsockettradingservice.domain.TransactionStatus;
import fr.stefangeorgesco.rsockettradingservice.domain.TransactionType;

public record TransactionResponse(String userId,
                                  TransactionType type,
                                  int amount,
                                  TransactionStatus status) {
}
