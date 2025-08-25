package com.zinolynn.banca.domain.controllers;


import com.stripe.model.Account;
import com.zinolynn.banca.domain.services.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/account")
    public Object createAccount(@RequestParam String email, @RequestParam String country) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "custom");
        params.put("country", country);
        params.put("email", email);
        params.put("business_type", "individual");

        Account account = Account.create(params);

        // Build safe response for client
        Map<String, Object> response = new HashMap<>();
        response.put("id", account.getId());
        response.put("email", account.getEmail());
        response.put("country", account.getCountry());
        response.put("charges_enabled", account.getChargesEnabled());
        response.put("payouts_enabled", account.getPayoutsEnabled());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment-intent")
    public Object createPaymentIntent(@RequestParam Long amount,
                                      @RequestParam String currency,
                                      @RequestParam String customerId) throws Exception {
        return stripeService.createPaymentIntent(amount, currency, customerId);
    }

    @PostMapping("/transfer")
    public Object transfer(@RequestParam Long amount,
                           @RequestParam String currency,
                           @RequestParam String destinationAccountId) throws Exception {
        return stripeService.transferToConnectedAccount(amount, currency, destinationAccountId);
    }

    @PostMapping("/payout")
    public Object payout(@RequestParam Long amount, @RequestParam String currency) throws Exception {
        return stripeService.createPayout(amount, currency);
    }

    @GetMapping("/transactions")
    public Object getTransactions() throws Exception {
        return stripeService.getTransactions();
    }

    @PostMapping("/attach-card")
    public Object attachCard(@RequestParam String paymentMethodId,
                             @RequestParam String customerId) throws Exception {
        return stripeService.attachCardToCustomer(paymentMethodId, customerId);
    }

    @PostMapping("/customer-with-card")
    public Object createCustomerWithCard(@RequestParam String email,
                                         @RequestParam String token) throws Exception {
        return stripeService.createCustomerWithCard(email, token);
    }
}
