package com.predators.service;

import com.predators.dto.product.ProductCountDto;
import com.predators.dto.product.ProductFilterDto;
import com.predators.dto.product.ProductRequestDto;
import com.predators.entity.Category;
import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;
import com.predators.exception.DiscountGraterThanPriceException;
import com.predators.exception.DiscountNotFoundException;
import com.predators.exception.ProductNotFoundException;
import com.predators.repository.ProductJpaRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductJpaRepository repository;

    private final CategoryService categoryService;

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Page<Product> getAll(ProductFilterDto filter, int page, int size, String[] sortParams) {
        List<Sort.Order> orders = new ArrayList<>();

        for (String param : sortParams) {
            if (param == null || param.isBlank()) continue;

            String[] split = param.split(";");

            String field = split[0].trim();
            String direction = (split.length > 1) ? split[1].trim().toUpperCase() : "ASC";

            try {
                Sort.Direction dir = Sort.Direction.valueOf(direction);
                orders.add(new Sort.Order(dir, field));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sort direction: " + direction + " for field: " + field);
            }
        }

        if (orders.isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.ASC, "name")); // fallback
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Specification<Product> spec = withFilters(filter);
        return repository.findAll(spec, pageable);
    }

    @Override
    public Product create(Product product) {
        product.setCreatedAt(Timestamp.from(Instant.now()));
        return repository.save(product);
    }

    @Override
    public Product getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with " + id + " Not Found"));
    }

    @Override
    public void delete(Long id) {
        Product product = getById(id);
        repository.deleteById(product.getId());
    }

    @Override
    public Product update(Long id, ProductRequestDto product) {
        Product productByDB = getById(id);
        if (product.name() != null && !product.name().isBlank()) {
            productByDB.setName(product.name());
        }
        if (product.description() != null && !product.description().isBlank()) {
            productByDB.setDescription(product.description());
        }
        if (product.price() != null) {
            productByDB.setPrice(product.price());
        }
        if (product.imageUrl() != null) {
            productByDB.setImageUrl(product.imageUrl());
        }
        if (product.categoryId() != null) {
            Category categoryById = categoryService.getById(product.categoryId());
            updateCategory(id, categoryById);
        }
        productByDB.setUpdatedAt(Timestamp.from(Instant.now()));
        return repository.save(productByDB);
    }

    @Override
    public void updateCategory(Long id, Category category) {
        Product product = getById(id);
        product.setCategory(category);
        repository.save(product);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return repository.findByCategory_Id(categoryId);
    }

    @Override
    public Product setDiscount(Long id, BigDecimal discount) {
        Product product = getById(id);

        if (product.getPrice().compareTo(discount) > 0) {
            product.setDiscountPrice(discount);
            product.setUpdatedAt(Timestamp.from(Instant.now()));
            return create(product);
        }

        throw new DiscountGraterThanPriceException("Discount can't be grater than Price!");
    }

    @Override
    public Product getDayProduct() {
        List<Product> productRepo = repository.findAllByDiscountPriceIsNotNull();

        Comparator<Product> comparator = Comparator.comparing(Product::getDiscountPrice).reversed();
        PriorityQueue<Product> priorityQueue = new PriorityQueue<>(comparator);
        priorityQueue.addAll(productRepo);

        if (priorityQueue.isEmpty()) {
            throw new DiscountNotFoundException("Product with discount is not found");
        }
        return priorityQueue.peek();
    }

    private Specification<Product> withFilters(ProductFilterDto filter) {

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

                if (Boolean.TRUE.equals(filter.discountPrice())) {
                    predicates.add(cb.isNotNull(root.get("discountPrice")));
                    predicates.add(cb.lessThan(root.get("discountPrice"), root.get("price")));
                } else if (Boolean.FALSE.equals(filter.discountPrice())) {
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

    @Override
    public List<ProductCountDto> findTopProductsAndCountsByOrderStatus(OrderStatus status, int limit) {
        return repository.findTopProductsAndCountsByOrderStatus(status, limit);
    }

    @Override
    public Set<Product> findByStatusAndUpdatedAtBeforeThreshold(OrderStatus status, Timestamp data) {
        return repository.findProductsFromOrdersByStatusAndUpdatedAtBefore(status, data);
    }
}

