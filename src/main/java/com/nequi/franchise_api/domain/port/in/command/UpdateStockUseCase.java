package com.nequi.franchise_api.domain.port.in.command;

import com.nequi.franchise_api.domain.model.entity.Product;
import reactor.core.publisher.Mono;

public interface UpdateStockUseCase {

    Mono<Product> updateStock(UpdateStockCommand command);

    record UpdateStockCommand(String productId, Integer stock) {
        public UpdateStockCommand {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be null or empty");
            }
            if (stock == null || stock < 0) {
                throw new IllegalArgumentException("Stock must be a non-negative number");
            }
        }
    }
}
