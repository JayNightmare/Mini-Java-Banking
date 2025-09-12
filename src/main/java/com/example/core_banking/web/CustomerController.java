package com.example.core_banking.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.core_banking.domain.model.Customer;
import com.example.core_banking.repo.CustomerRepo;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerRepo repo;

    public CustomerController(CustomerRepo repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Customer dto) {
        dto.setId(UUID.randomUUID());
        repo.save(dto);
        return ResponseEntity.ok(Map.of("id", dto.getId()));
    }
}
