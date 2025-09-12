package com.example.core_banking.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.core_banking.domain.model.TransactionRecord;

public interface TxnRepo extends JpaRepository<TransactionRecord, UUID> {
    Optional<TransactionRecord> findByExternalId(String id);
}
