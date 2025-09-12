package com.example.core_banking.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.core_banking.repo.CustomerRepo;
import com.example.core_banking.repo.AccountRepo;
import com.example.core_banking.repo.TxnRepo;

@Controller
public class DashboardController {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TxnRepo txnRepo;

    @GetMapping("/")
    public String dashboard(Model model) {
        // Add statistics for the dashboard
        long totalCustomers = customerRepo.count();
        long totalAccounts = accountRepo.count();
        long totalTransactions = txnRepo.count();

        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalAccounts", totalAccounts);
        model.addAttribute("totalTransactions", totalTransactions);

        return "dashboard_simple";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}