package com.predators.repository;

import com.predators.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByCategory_Id(Long categoryId);

    @Query("SELECT product FROM Product product WHERE product.discountPrice > 0")
    List<Product> findProductWithDiscount();
}
