package com.mymart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mymart.model.PriceRange;

public interface PriceRangeRepository extends JpaRepository<PriceRange, Long> {
}