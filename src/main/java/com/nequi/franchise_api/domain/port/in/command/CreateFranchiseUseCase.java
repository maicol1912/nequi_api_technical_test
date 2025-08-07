package com.nequi.franchise_api.domain.port.in.command;

import com.nequi.franchise_api.domain.model.entity.Franchise;
import reactor.core.publisher.Mono;

public interface CreateFranchiseUseCase {

    Mono<Franchise> createFranchise(CreateFranchiseCommand command);

    record CreateFranchiseCommand(String name) {
        public CreateFranchiseCommand {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Franchise name cannot be null or empty");
            }
        }
    }
}