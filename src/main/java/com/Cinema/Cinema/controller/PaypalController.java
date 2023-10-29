package com.Cinema.Cinema.controller;

import com.Cinema.Cinema.model.Order;
import com.Cinema.Cinema.model.ShoppingCart;
import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.repository.UserRepository;
import com.Cinema.Cinema.service.ShoppingCartService;
import com.Cinema.Cinema.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings( "ALL" )
@Controller
public class PaypalController {
    @Autowired
    PaypalService service;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserRepository userRepository;
    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
//    Return Buy Page
    @GetMapping("/buy")
    public String home(@AuthenticationPrincipal UserDetails loggedUser, HttpServletRequest request, Model model) {
        String sessionToken = (String) request.getSession(true).getAttribute("sessionToken" );
        String email = loggedUser.getUsername ();
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list",user);
        if (sessionToken == null){
            model.addAttribute ("shoppingCart",new ShoppingCart ());
        }else {
            ShoppingCart shoppingCart = shoppingCartService.getShoppingCartBySessionToken(sessionToken);
            model.addAttribute ("shoppingCart",shoppingCart);
        }
        return "buy";
    }
//    Confirm Payment
    @PostMapping("/pay")
    public String payment(@ModelAttribute("order") Order order) {
        try {
            Payment payment = service.createPayment(order.getPrice (), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }
        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/main";
    }
    //Cancel Pay Return Page
    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "main";
    }
//Success Pay Return Page
    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "main";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/main";
    }
}