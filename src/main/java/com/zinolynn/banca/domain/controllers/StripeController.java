package com.zinolynn.banca.domain.controllers;


import com.zinolynn.banca.domain.services.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/account")
    public Object createAccount(@RequestParam String email, @RequestParam String country) throws Exception {
        return stripeService.createConnectedAccount(email, country);
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
