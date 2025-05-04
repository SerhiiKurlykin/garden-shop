package com.predators.repository;

import com.predators.entity.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<ShopUser, Long> {
    Optional<ShopUser> findByEmail(String email);
}
