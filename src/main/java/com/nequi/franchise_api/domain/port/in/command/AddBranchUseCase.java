package com.nequi.franchise_api.domain.port.in.command;

import com.nequi.franchise_api.domain.model.entity.Branch;
import reactor.core.publisher.Mono;

public interface AddBranchUseCase {

    Mono<Branch> addBranch(AddBranchCommand command);

    record AddBranchCommand(String franchiseId, String name) {
        public AddBranchCommand {
            if (franchiseId == null || franchiseId.trim().isEmpty()) {
                throw new IllegalArgumentException("Franchise ID cannot be null or empty");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Branch name cannot be null or empty");
            }
        }
    }
}
