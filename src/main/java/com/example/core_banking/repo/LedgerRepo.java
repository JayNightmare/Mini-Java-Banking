package com.example.core_banking.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.core_banking.domain.model.LedgerEntry;

public interface LedgerRepo extends JpaRepository<LedgerEntry, UUID> {
    @Query("""
            SELECT coalesce(sum(case when l.direction='CREDIT' then l.amountCents else -l.amountCents end), 0)
            from LedgerEntry l where l.accountId = :accountId
            """)
    Long balanceCents(UUID accountId);
}
