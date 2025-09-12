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

import com.example.core_banking.domain.model.Customer;
import com.example.core_banking.repo.CustomerRepo;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {

    @Autowired
    private CustomerRepo customerRepo;

    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerRepo.findAll();
        model.addAttribute("customers", customers);
        return "customers/list_simple";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/create_simple";
    }

    @PostMapping("/create")
    public String createCustomer(@Valid @ModelAttribute Customer customer,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "customers/create_simple";
        }

        customer.setId(UUID.randomUUID());
        customer.setKycStatus("PENDING");
        customer.setCreatedAt(Instant.now());

        customerRepo.save(customer);
        redirectAttributes.addFlashAttribute("successMessage",
                "Customer created successfully with ID: " + customer.getId());

        return "redirect:/customers";
    }
}