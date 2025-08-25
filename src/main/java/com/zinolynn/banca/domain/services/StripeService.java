package com.zinolynn.banca.domain.services;


import com.stripe.model.*;
import com.stripe.param.*;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    public Account createConnectedAccount(String email, String country) throws Exception {
        AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.EXPRESS)
                .setCountry(country)
                .setEmail(email)
                .build();

        return Account.create(params);
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency, String customerId) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount) // in cents
                .setCurrency(currency)
                .setCustomer(customerId)
                .build();

        return PaymentIntent.create(params);
    }

    public Transfer transferToConnectedAccount(Long amount, String currency, String destinationAccountId) throws Exception {
        TransferCreateParams params = TransferCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setDestination(destinationAccountId)
                .build();

        return Transfer.create(params);
    }

    public Payout createPayout(Long amount, String currency) throws Exception {
        PayoutCreateParams params = PayoutCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .build();

        return Payout.create(params);
    }

    public BalanceTransactionCollection getTransactions() throws Exception {
        BalanceTransactionListParams params = BalanceTransactionListParams.builder()
                .setLimit(10L)
                .build();

        return BalanceTransaction.list(params);
    }
    public PaymentMethod attachCardToCustomer(String paymentMethodId, String customerId) throws Exception {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);

        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();

        return paymentMethod.attach(params);
    }


    public Customer createCustomerWithCard(String email, String token) throws Exception {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .setSource(token) // "tok_visa" from frontend
                .build();

        return Customer.create(params);
    }

}
