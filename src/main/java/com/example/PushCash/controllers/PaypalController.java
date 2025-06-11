package com.example.PushCash.controllers;

import com.example.PushCash.models.ServiceProvider;
import com.example.PushCash.services.PaypalService;
import com.example.PushCash.services.ServiceProviderService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller

public class PaypalController {

    private final PaypalService paypalService;
    private final ServiceProviderService serviceProviderService;
    @Autowired
    PaypalController(PaypalService paypalService, ServiceProviderService serviceProviderService){
        this.paypalService=paypalService;
        this.serviceProviderService = serviceProviderService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/payment/create")
    public RedirectView createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
            ,@RequestParam("providerId") Long providerId
    ) {
        try {
            ServiceProvider provider = serviceProviderService.findById(providerId).orElseThrow();
            String clientId = provider.getClientId();
            String clientSecret = provider.getClientSecret();
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl,
                    clientId,
                    clientSecret
            );

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println("error occurred :: "+e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            System.out.println("error occurred :: "+e);
        }
        return "paymentError";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }
}