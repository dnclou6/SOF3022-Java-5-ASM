package com.example.AsmGD1.repository;

import com.example.AsmGD1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findTop3ByOrderByIdAsc();
    List<Product> findByNameContainingIgnoreCase(String name);
}
