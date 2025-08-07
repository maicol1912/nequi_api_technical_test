package com.nequi.franchise_api.domain.port.in.command;

import com.nequi.franchise_api.domain.model.entity.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductUseCase {

    Mono<Product> updateProduct(UpdateProductCommand command);

    record UpdateProductCommand(String productId, String name) {
        public UpdateProductCommand {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be null or empty");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be null or empty");
            }
        }
    }
}
