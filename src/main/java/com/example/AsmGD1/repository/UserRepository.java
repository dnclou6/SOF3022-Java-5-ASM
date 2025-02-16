package com.example.AsmGD1.repository;

import com.example.AsmGD1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
}
