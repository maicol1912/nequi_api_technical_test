package com.nequi.franchise_api.domain.port.in.command;

import reactor.core.publisher.Mono;

public interface RemoveProductUseCase {

    Mono<Void> removeProduct(RemoveProductCommand command);

    record RemoveProductCommand(String productId) {
        public RemoveProductCommand {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be null or empty");
            }
        }
    }
}
