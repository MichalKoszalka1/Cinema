package com.Cinema.Cinema.controller;

import com.Cinema.Cinema.model.FavoriteFilmCart;
import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.repository.UserRepository;
import com.Cinema.Cinema.service.FilmService;
import com.Cinema.Cinema.service.FavoriteFilmService;
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
public class FavoriteFilmController {
    @Autowired
    private FilmService filmService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavoriteFilmService favoriteFilmService;
//    Add Film To Favorite
    @PostMapping("/addFilmToFavorite")
    public String addToFavorite(@AuthenticationPrincipal UserDetails loggedUser,Model model, HttpServletRequest request, @RequestParam("id")Long id){
        String sessionToken = (String) request.getSession(true).getAttribute("favoriteSessionToken" );
        String email = loggedUser.getUsername ();
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list",user);
        if (sessionToken == null){
            sessionToken = UUID.randomUUID ().toString ();
            request.getSession ().setAttribute ("favoriteSessionToken",sessionToken);
            favoriteFilmService.createAFavoriteFilm (id,sessionToken);
        }else {
            favoriteFilmService.addFavoriteFilm (id,sessionToken);
        }
        return "redirect:/Favorite";
    }
//    Show Favorite Page
    @GetMapping("/Favorite")
    public String FavoritePageFilm(@AuthenticationPrincipal UserDetails loggedUser, HttpServletRequest request, Model model) {
        String sessionToken = (String) request.getSession(true).getAttribute("favoriteSessionToken" );
        String email = loggedUser.getUsername ();
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list",user);
        if (sessionToken == null){
            model.addAttribute ("favoriteCart",new FavoriteFilmCart ());
        }else {
            FavoriteFilmCart favoriteFilmCart = favoriteFilmService.getFavoriteFilmBySessionToken (sessionToken);
            model.addAttribute ("favoriteCart",favoriteFilmCart);
        }
        return "Favorite";
    }
//  Remove Film By Id
    @GetMapping("/removeFavoriteFilm/{id}")
    public String removeItem(@PathVariable("id") Long id, HttpServletRequest request) {
        String sessionToken = (String) request.getSession(false).getAttribute("favoriteSessionToken");
        System.out.println("got here ");
        favoriteFilmService.removeFavoriteFilm (id,sessionToken);
        return "redirect:/Favorite";
    }
//    Delete All Favorite Films
    @GetMapping("/clearFavoritePage")
    public String deleteAllFavoriteFilm(HttpServletRequest request) {
        String sessionToken = (String) request.getSession(false).getAttribute("favoriteSessionToken");
        request.getSession(false).removeAttribute("favoriteSessionToken");
        favoriteFilmService.deleteAllFavoriteFilm (sessionToken);
        return "redirect:/Favorite";
    }
}
