package com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import com.nequi.franchise_api.infrastructure.dto.response.ProductMaxStockProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Repository
public interface FranchiseQueryR2dbcRepository extends R2dbcRepository<FranchiseEntity, String> {

    @Query("SELECT f.*, b.*, p.* " +
            "FROM franchises f " +
            "LEFT JOIN branches b ON f.id = b.franchise_id " +
            "LEFT JOIN products p ON b.id = p.branch_id " +
            "WHERE f.id = :franchiseId")
    Flux<Object[]> findFranchiseWithBranchesAndProducts(String franchiseId);
}