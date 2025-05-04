package com.predators.specification;

import com.predators.dto.product.ProductFilterDto;
import com.predators.entity.Category;
import com.predators.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> withFilters(ProductFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter != null) {

                if (filter.minPrice() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.minPrice()));
                }

                if (filter.maxPrice() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.maxPrice()));
                }

                if (filter.categoryId() != null) {
                    Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);
                    predicates.add(cb.equal(categoryJoin.get("id"), filter.categoryId()));
                }

                if (Boolean.TRUE.equals(filter.discount())) {
                    predicates.add(cb.isNotNull(root.get("discountPrice")));
                    predicates.add(cb.lessThan(root.get("discountPrice"), root.get("price")));
                } else if (Boolean.FALSE.equals(filter.discount())) {
                    predicates.add(
                            cb.or(
                                    cb.isNull(root.get("discountPrice")),
                                    cb.greaterThanOrEqualTo(root.get("discountPrice"), root.get("price"))
                            )
                    );
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
