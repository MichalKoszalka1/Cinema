package com.Cinema.Cinema.controller;

import com.Cinema.Cinema.model.ShoppingCart;
import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.repository.UserRepository;
import com.Cinema.Cinema.service.FilmService;
import com.Cinema.Cinema.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@SuppressWarnings( "ALL" )
@Controller
public class CartController {

    @Autowired
    private FilmService filmService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartService  shoppingCartService;
//   Add Film To Shopping Cart
    @PostMapping("/addToCart")
    public String addToCart(@AuthenticationPrincipal UserDetails loggedUser, HttpServletRequest request, Model model, @RequestParam("id")Long id,
                            @RequestParam ("ticket") int ticket){
        String sessionToken =(String) request.getSession (true).getAttribute ("sessionToken");
        String email = loggedUser.getUsername ();
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list",user);
        if (sessionToken == null){
            sessionToken = UUID.randomUUID ().toString ();
        request.getSession ().setAttribute ("sessionToken",sessionToken);
        shoppingCartService.addShoppingCart(id,sessionToken,ticket);

        }else {
             shoppingCartService.addToExistingShoppingCart (id,sessionToken,ticket);
        }
        return "redirect:/shoppingCart";
    }
//     Show Shopping Cart Page
    @GetMapping("/shoppingCart")
    public String showShoppingCartView(@AuthenticationPrincipal UserDetails loggedUser,HttpServletRequest request, Model model) {
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
        return "shoppingCart";
    }
    @PostMapping("/updateShoppingCart")
    public String updateCartItem(@AuthenticationPrincipal UserDetails loggedUser,Model model,@RequestParam("item_id") Long id, @RequestParam("quantity") int quantity) {
        String email = loggedUser.getUsername ();
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list",user);
        shoppingCartService.updateShoppingCartItem(id,quantity);
        return "redirect:shoppingCart";
    }
//    Remove One Film
    @GetMapping("/removeCartItem/{id}")
    public String removeItem(@AuthenticationPrincipal UserDetails loggedUser,Model model,@PathVariable("id") Long id, HttpServletRequest request) {
        String sessionToken = (String) request.getSession(false).getAttribute("sessionToken");
        String email = loggedUser.getUsername ();
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list",user);
        System.out.println("got here ");
        shoppingCartService.removeCartIemFromShoppingCart(id,sessionToken);
        return "redirect:/shoppingCart";
    }
//     Clear Whole Shopping Cart
    @GetMapping("/clearShoppingCart")
    public String clearShoopiString(@AuthenticationPrincipal UserDetails loggedUser,Model model,HttpServletRequest request) {
        String sessionToken = (String) request.getSession(false).getAttribute("sessionToken");
        String email = loggedUser.getUsername ();
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list",user);
        request.getSession(false).removeAttribute("sessionToken");
        shoppingCartService.clearShoppingCart(sessionToken);
        return "redirect:/shoppingCart";
    }
}
