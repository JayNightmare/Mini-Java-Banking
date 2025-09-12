package com.example.core_banking.domain.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, name = "txn_id")
    private UUID txnId;

    @Column(nullable = false, name = "account_id")
    private UUID accountId;

    @Column(nullable = false)
    private String direction; // DEBIT or CREDIT

    @Column(nullable = false, name = "amount_cents")
    private Long amountCents;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, name = "booked_at")
    private Instant bookedAt = Instant.now();

    private String narrative;

    // * Getters and Setters * //

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTxnId() {
        return this.txnId;
    }

    public void setTxnId(UUID txnId) {
        this.txnId = txnId;
    }

    public UUID getAccountId() {
        return this.accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Long getAmountCents() {
        return this.amountCents;
    }

    public void setAmountCents(Long amountCents) {
        this.amountCents = amountCents;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getBookedAt() {
        return this.bookedAt;
    }

    public void setBookedAt(Instant bookedAt) {
        this.bookedAt = bookedAt;
    }

    public String getNarrative() {
        return this.narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }
}
