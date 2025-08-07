package com.nequi.franchise_api.infrastructure.adapter.out.persistence.adapter;

import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.*;
import com.nequi.franchise_api.domain.port.out.FranchiseQueryRepository;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.BranchEntity;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository.FranchiseQueryR2dbcRepository;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository.ProductQueryRepository;
import com.nequi.franchise_api.infrastructure.dto.response.ProductMaxStockProjection;
import com.nequi.franchise_api.infrastructure.dto.response.ProductMaxStockResponse;
import com.nequi.franchise_api.infrastructure.mapper.BranchMapper;
import com.nequi.franchise_api.infrastructure.mapper.FranchiseMapper;
import com.nequi.franchise_api.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseQueryRepositoryAdapter implements FranchiseQueryRepository {

    private final FranchiseQueryR2dbcRepository franchiseQueryR2dbcRepository;
    private final ProductQueryRepository productQueryRepository;
    private final FranchiseMapper franchiseMapper;
    private final BranchMapper branchMapper;
    private final ProductMapper productMapper;

    @Override
    public Mono<FranchiseAggregate> findFranchiseWithBranchesAndProducts(FranchiseId franchiseId) {
        log.debug("Finding franchise with branches and products: {}", franchiseId);

        return franchiseQueryR2dbcRepository.findFranchiseWithBranchesAndProducts(String.valueOf(franchiseId.getValue()))
                .collectList()
                .map(this::buildFranchiseAggregate)
                .doOnSuccess(aggregate -> log.debug("Franchise aggregate built: {}", aggregate.getId()));
    }

    @Override
    public Flux<ProductMaxStock> findProductsWithMaxStockByFranchise(FranchiseId franchiseId) {
        log.debug("Finding products with max stock for franchise: {}", franchiseId);

        return productQueryRepository.findProductsWithMaxStockByFranchise(String.valueOf(franchiseId.getValue()))
                .map(this::mapProjectionToProductMaxStock)
                .doOnComplete(() -> log.debug("Products with max stock retrieved for franchise: {}", franchiseId));
    }


    private FranchiseAggregate buildFranchiseAggregate(List<Object[]> rows) {
        if (rows.isEmpty()) {
            return null;
        }

        FranchiseEntity franchiseEntity = null;
        List<BranchEntity> branches = new ArrayList<>();
        List<ProductEntity> products = new ArrayList<>();
        Map<String, BranchEntity> branchMap = new HashMap<>(); // Para evitar duplicados de sucursales

        for (Object[] row : rows) {
            FranchiseEntity currentFranchise = (FranchiseEntity) row[0];
            BranchEntity branchEntity = row.length > 1 && row[1] != null ? (BranchEntity) row[1] : null;
            ProductEntity productEntity = row.length > 2 && row[2] != null ? (ProductEntity) row[2] : null;

            if (franchiseEntity == null) {
                franchiseEntity = currentFranchise;
            }

            if (branchEntity != null && !branchMap.containsKey(branchEntity.getId().toString())) {
                branchMap.put(branchEntity.getId().toString(), branchEntity);
                branches.add(branchEntity);
            }

            if (productEntity != null) {
                products.add(productEntity);
            }
        }

        if (franchiseEntity == null) {
            return null;
        }

        List<Branch> domainBranches = branches.stream()
                .map(branchMapper::toDomain)
                .collect(Collectors.toList());

        List<Product> domainProducts = products.stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());

        return new FranchiseAggregate(
                franchiseMapper.toDomain(franchiseEntity),
                domainBranches,
                domainProducts
        );
    }

    private ProductMaxStock mapProjectionToProductMaxStock(ProductMaxStockProjection projection) {
        return ProductMaxStock.builder()
                .productId(ProductId.of(projection.getId()))
                .productName(projection.getName())
                .branchId(BranchId.of(projection.getBranch_id()))
                .branchName(projection.getBranch_name())
                .stock(projection.getStock())
                .build();
    }
}