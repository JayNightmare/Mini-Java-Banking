package com.example.core_banking.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.core_banking.domain.model.Account;
import com.example.core_banking.repo.AccountRepo;
import com.example.core_banking.repo.LedgerRepo;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountRepo accounts;
    private final LedgerRepo ledger;

    public AccountController(AccountRepo a, LedgerRepo l) {
        this.accounts = a;
        this.ledger = l;
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody Map<String, Object> req) {
        var account = new Account();
        account.setCustomerId(UUID.fromString(req.get("customerId").toString()));
        account.setCurrency(req.get("currency").toString());
        account.setIban("GB" + UUID.randomUUID().toString().replace("-", "").substring(0, 18));
        accounts.save(account);
        return Map.of("id", account.getId(), "iban", account.getIban());
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable("id") UUID id) {
        var account = accounts.findById(id).orElseThrow();
        var balance = Optional.ofNullable(ledger.balanceCents(id)).orElse(0L);
        return Map.of("id", account.getId(), "iban", account.getIban(), "currency", account.getCurrency(), "balance",
                balance);
    }
}
