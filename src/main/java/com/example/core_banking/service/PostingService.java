package com.example.core_banking.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.core_banking.domain.model.LedgerEntry;
import com.example.core_banking.domain.model.TransactionRecord;
import com.example.core_banking.repo.AccountRepo;
import com.example.core_banking.repo.LedgerRepo;
import com.example.core_banking.repo.TxnRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PostingService {
    private final AccountRepo accounts;
    private final TxnRepo txns;
    private final LedgerRepo ledgers;

    public PostingService(AccountRepo accounts, TxnRepo txns, LedgerRepo ledgers) {
        this.accounts = accounts;
        this.txns = txns;
        this.ledgers = ledgers;
    }

    public UUID deposit(UUID accountId, long cents, String currency, String idemKey) {
        if (idemKey != null) {
            var prior = txns.findByExternalId(idemKey);
            if (prior != null)
                return prior.get().getId();
        }

        // ? remove ? //
        @SuppressWarnings("unused")
        var acc = accounts.findById(accountId).orElseThrow();

        var txn = new TransactionRecord();
        txn.setExternalId(idemKey);
        txn.setType("DEPOSIT");
        txn.setStatus("PENDING");
        txns.saveAndFlush(txn);

        post(txn.getId(), null, accountId, cents, currency, "DEPOSIT");
        markBooked(txn);

        return txn.getId();
    }

    private void post(UUID txnId, UUID fromId, UUID toId, long cents, String cur, String narrative) {
        if (fromId != null) {
            ledgers.save(newLedger(txnId, fromId, "DEBIT", cents, cur, narrative));
        }
        if (toId != null) {
            ledgers.save(newLedger(txnId, toId, "CREDIT", cents, cur, narrative));
        }
    }

    private LedgerEntry newLedger(UUID txnId, UUID accountId, String dir, long cents, String cur, String narrative) {
        var entry = new LedgerEntry();
        entry.setTxnId(txnId);
        entry.setAccountId(accountId);
        entry.setAmountCents(cents);
        entry.setCurrency(cur);
        entry.setDirection(dir);
        entry.setNarrative(narrative);
        return entry;
    }

    private void markBooked(TransactionRecord t) {
        t.setStatus("BOOKED");
        t.setBookedAt(Instant.now());
        txns.save(t);
    }
}
