package com.nequi.franchise_api.domain.model.aggregate;

import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class FranchiseAggregate {

    private final Franchise franchise;
    private final List<Branch> branches;
    private final List<Product> products;

    public FranchiseId getId() {
        return franchise.getId();
    }

    public String getName() {
        return franchise.getName().getValue();
    }

    public Map<BranchId, List<Product>> getProductsByBranch() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getBranchId));
    }

    public List<Product> getProductsForBranch(BranchId branchId) {
        return products.stream()
                .filter(product -> product.getBranchId().equals(branchId))
                .toList();
    }

    public int getTotalBranches() {
        return branches.size();
    }

    public int getTotalProducts() {
        return products.size();
    }

    public boolean hasBranches() {
        return !branches.isEmpty();
    }

    public boolean hasProducts() {
        return !products.isEmpty();
    }
}
