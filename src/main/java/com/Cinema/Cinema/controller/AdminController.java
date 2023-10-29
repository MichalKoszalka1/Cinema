package com.Cinema.Cinema.controller;

import com.Cinema.Cinema.model.Film;
import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.repository.UserRepository;
import com.Cinema.Cinema.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings( "ALL" )
@Controller
public class AdminController {
    @Autowired
    private FilmService filmService;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;
//    Create Film -Admin
    @GetMapping("/createFilm")
    public String createFilmView(@AuthenticationPrincipal UserDetails loggedUser,Model model){
        String email = loggedUser.getUsername ( );
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list", user);
        return "/createFilm.html";
    }
//    Back To Main Page
    @GetMapping("/back")
    public String main(){
        return "redirect:/main";
    }
//    Create A New Film
    @PostMapping("/addP")
    public String createFilm(
            @RequestParam("file") MultipartFile  file,
            @RequestParam("title")String title,
            @RequestParam("ticket") int  ticket,
            @RequestParam("duration")int duration,
            @RequestParam("category")String category,
            @RequestParam("description")String description,
            @RequestParam("price")Double price,
            @RequestParam("spot")String spot,
            @RequestParam("date") java.sql.Date date,
            @RequestParam("time") String time,
            @RequestParam("age") int age ,
            Model model,@AuthenticationPrincipal UserDetails loggedUser) {
        String keyword= null;
        filmService.saveFilmToDataBase (file,title,ticket,duration,category,description,price,spot,date,time ,age);
        return userController.listByPage (loggedUser,model,keyword);
    }
//    Remove Film By Admin
    @GetMapping("/removeFilm/{id}")
    public String removeFilm(@PathVariable ("id") Long id){
        filmService.removeAccount (id);
        return "redirect:/main";
    }
//    Film Details
    @GetMapping("/film/details/{id}")
    public String showIndex(@AuthenticationPrincipal UserDetails loggedUser,@PathVariable("id") Long id, Model model) {
        if (loggedUser != null) {
            String email = loggedUser.getUsername ( );
            User user = userRepository.findByEmail (email);
            model.addAttribute ("list", user);
            Film film = filmService.findById (id);
            model.addAttribute("film", film);
        }else if (loggedUser == null){
            Film film = filmService.findById (id);
            model.addAttribute("film", film);
        }
        return "filmDetail";
    }
}
