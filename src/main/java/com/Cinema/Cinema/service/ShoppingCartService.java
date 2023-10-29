package com.Cinema.Cinema.service;

import com.Cinema.Cinema.model.CartItem;
import com.Cinema.Cinema.model.Film;
import com.Cinema.Cinema.model.ShoppingCart;
import com.Cinema.Cinema.repository.CartItemRepository;
import com.Cinema.Cinema.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
@SuppressWarnings( "ALL" )
@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private FilmService filmService;

    @Autowired
    private CartItemRepository cartItemRepository;

//    Add To Shopping Cart
    public ShoppingCart addShoppingCart(Long id, String sessionToken, int quantity) {
        ShoppingCart shoppingCart = new ShoppingCart ();
        CartItem cartItem = new CartItem ();
        cartItem.setQuantity (quantity);
        cartItem.setDate (new Date ());
        cartItem.setFilm (filmService.findById (id));
        shoppingCart.getItems ().add (cartItem);
        shoppingCart.setTokenSession (sessionToken);
        shoppingCart.setDate (new Date ());
       return shoppingCartRepository.save (shoppingCart);
    }
//  Add To Existing Shopping Cart
    public ShoppingCart addToExistingShoppingCart(Long id, String sessionToken, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findBySessionToken (sessionToken);
        Film film = filmService.findById (id);
        Boolean filmDoesExistInTheCart =false;
        if (shoppingCart != null) {
            Collection<CartItem> items = shoppingCart.getItems ( );
            for (CartItem item : items) {
                if (item.getFilm ().equals (film)) {
                   filmDoesExistInTheCart =true;
                   item.setQuantity (item.getQuantity () + quantity);
                   shoppingCart.setItems (items);
                    return shoppingCartRepository.saveAndFlush (shoppingCart);
                }
            }
        }
        if (!filmDoesExistInTheCart && (shoppingCart != null)){
        CartItem cartItem = new CartItem ();
        cartItem.setDate (new Date (  ));
        cartItem.setQuantity (quantity);
        cartItem.setFilm (film);
        shoppingCart.getItems ().add (cartItem);
        return shoppingCartRepository.saveAndFlush (shoppingCart);
    }
        return this.addShoppingCart (id,sessionToken,quantity);
    }
//    Get Shopping Cart BY Session Token
    public ShoppingCart getShoppingCartBySessionToken(String sessionToken) {
        return  shoppingCartRepository.findBySessionToken(sessionToken);
    }
    public CartItem updateShoppingCartItem(Long id, int quantity) {
        CartItem cartItem = cartItemRepository.findById(id).get();
        cartItem.setQuantity(quantity);
        return cartItemRepository.saveAndFlush(cartItem);

    }
//     Remove Film From Shopping Cart By Id
    public ShoppingCart removeCartIemFromShoppingCart(Long id, String sessionToken) {
        ShoppingCart shoppingCart = shoppingCartRepository.findBySessionToken(sessionToken);
        Collection<CartItem> items = shoppingCart.getItems ();
        CartItem cartItem = null;
        for(CartItem item : items) {
            if(Objects.equals (item.getId ( ), id)) {
                cartItem = item;
            }
        }
        items.remove(cartItem);
        cartItemRepository.delete(cartItem);
        shoppingCart.setItems (items);
        return shoppingCartRepository.save(shoppingCart);
    }
//    Clear Shopping Cart
    public void clearShoppingCart(String sessionToken) {
        ShoppingCart shoppingCart = shoppingCartRepository.findBySessionToken(sessionToken);
        shoppingCartRepository.delete(shoppingCart);
    }
}
