package com.example.core_banking.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.core_banking.domain.model.TransactionRecord;
import com.example.core_banking.domain.model.LedgerEntry;
import com.example.core_banking.domain.model.Account;
import com.example.core_banking.repo.TxnRepo;
import com.example.core_banking.repo.LedgerRepo;
import com.example.core_banking.repo.AccountRepo;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionWebController {

    @Autowired
    private TxnRepo txnRepo;

    @Autowired
    private LedgerRepo ledgerRepo;

    @Autowired
    private AccountRepo accountRepo;

    @GetMapping
    public String listTransactions(Model model) {
        List<TransactionRecord> transactions = txnRepo.findAll();
        model.addAttribute("transactions", transactions);
        return "transactions/list";
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model, @RequestParam(required = false) UUID fromAccount) {
        List<Account> accounts = accountRepo.findAll();
        model.addAttribute("accounts", accounts);
        model.addAttribute("preselectedAccount", fromAccount);
        return "transactions/transfer";
    }

    @PostMapping("/transfer")
    public String processTransfer(@RequestParam UUID fromAccountId,
            @RequestParam UUID toAccountId,
            @RequestParam Long amountCents,
            @RequestParam String currency,
            @RequestParam(required = false) String narrative,
            RedirectAttributes redirectAttributes) {

        try {
            // Validate accounts exist
            Optional<Account> fromAccount = accountRepo.findById(fromAccountId);
            Optional<Account> toAccount = accountRepo.findById(toAccountId);

            if (fromAccount.isEmpty() || toAccount.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid account(s) selected");
                return "redirect:/transactions/transfer";
            }

            if (fromAccountId.equals(toAccountId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cannot transfer to the same account");
                return "redirect:/transactions/transfer";
            }

            if (amountCents <= 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Amount must be positive");
                return "redirect:/transactions/transfer";
            }

            // Create transaction record
            TransactionRecord transaction = new TransactionRecord();
            transaction.setId(UUID.randomUUID());
            transaction.setExternalId("TXN-" + System.currentTimeMillis());
            transaction.setType("TRANSFER");
            transaction.setStatus("COMPLETED");
            transaction.setCreatedAt(Instant.now());
            transaction.setBookedAt(Instant.now());

            txnRepo.save(transaction);

            // Create debit entry
            LedgerEntry debitEntry = new LedgerEntry();
            debitEntry.setId(UUID.randomUUID());
            debitEntry.setTxnId(transaction.getId());
            debitEntry.setAccountId(fromAccountId);
            debitEntry.setDirection("DEBIT");
            debitEntry.setAmountCents(amountCents);
            debitEntry.setCurrency(currency);
            debitEntry.setBookedAt(Instant.now());
            debitEntry.setNarrative(narrative != null ? narrative : "Transfer to " + toAccount.get().getIban());

            ledgerRepo.save(debitEntry);

            // Create credit entry
            LedgerEntry creditEntry = new LedgerEntry();
            creditEntry.setId(UUID.randomUUID());
            creditEntry.setTxnId(transaction.getId());
            creditEntry.setAccountId(toAccountId);
            creditEntry.setDirection("CREDIT");
            creditEntry.setAmountCents(amountCents);
            creditEntry.setCurrency(currency);
            creditEntry.setBookedAt(Instant.now());
            creditEntry.setNarrative(narrative != null ? narrative : "Transfer from " + fromAccount.get().getIban());

            ledgerRepo.save(creditEntry);

            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Transfer of %s %.2f completed successfully",
                            currency, amountCents / 100.0));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Transfer failed: " + e.getMessage());
        }

        return "redirect:/transactions";
    }
}