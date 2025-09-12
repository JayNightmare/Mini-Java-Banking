package com.example.core_banking.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.core_banking.domain.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, UUID> {
}