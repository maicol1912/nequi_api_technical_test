package com.nequi.franchise_api.domain.port.in.command;

import com.nequi.franchise_api.domain.model.entity.Branch;
import reactor.core.publisher.Mono;

public interface UpdateBranchUseCase {

    Mono<Branch> updateBranch(UpdateBranchCommand command);

    record UpdateBranchCommand(String branchId, String name) {
        public UpdateBranchCommand {
            if (branchId == null || branchId.trim().isEmpty()) {
                throw new IllegalArgumentException("Branch ID cannot be null or empty");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Branch name cannot be null or empty");
            }
        }
    }
}
