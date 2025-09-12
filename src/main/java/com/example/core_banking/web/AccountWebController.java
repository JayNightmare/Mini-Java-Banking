package com.example.core_banking.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.core_banking.domain.model.Account;
import com.example.core_banking.domain.model.Customer;
import com.example.core_banking.repo.AccountRepo;
import com.example.core_banking.repo.CustomerRepo;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.Random;

@Controller
@RequestMapping("/accounts")
public class AccountWebController {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @GetMapping
    public String listAccounts(Model model) {
        List<Account> accounts = accountRepo.findAll();
        model.addAttribute("accounts", accounts);
        return "accounts/list_simple";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("account", new Account());
        List<Customer> customers = customerRepo.findAll();
        model.addAttribute("customers", customers);
        return "accounts/create";
    }

    @PostMapping("/create")
    public String createAccount(@Valid @ModelAttribute Account account,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Customer> customers = customerRepo.findAll();
            model.addAttribute("customers", customers);
            return "accounts/create";
        }

        account.setId(UUID.randomUUID());
        account.setIban(generateIban());
        account.setStatus("ACTIVE");
        account.setVersion(0);
        account.setCreatedAt(Instant.now());

        accountRepo.save(account);
        redirectAttributes.addFlashAttribute("successMessage",
                "Account created successfully with IBAN: " + account.getIban());

        return "redirect:/accounts";
    }

    private String generateIban() {
        // Simple IBAN generation for demo (GB + 22 digits)
        Random random = new Random();
        StringBuilder iban = new StringBuilder("GB");
        for (int i = 0; i < 22; i++) {
            iban.append(random.nextInt(10));
        }
        return iban.toString();
    }
}