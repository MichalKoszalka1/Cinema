package com.Cinema.Cinema.service;

import com.Cinema.Cinema.model.*;
import com.Cinema.Cinema.repository.FavoriteFilmCartRepository;
import com.Cinema.Cinema.repository.FavoriteFilmItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings( "ALL" )
@Service
public class FavoriteFilmService {
    @Autowired
    private FavoriteFilmCartRepository favoriteFilmCartRepository;
    @Autowired
    private FilmService filmService;
    @Autowired
    private FavoriteFilmItemRepository favoriteFilmItemRepository;
    //     Create Favorite Film
    public FavoriteFilmCart createAFavoriteFilm(Long id, String sessionToken) {
        FavoriteFilmCart favoriteFilmCart = new FavoriteFilmCart ();
        FavoriteFilmItem favoriteFilmItem = new FavoriteFilmItem ();
        favoriteFilmItem.setDate (new Date ());
        favoriteFilmItem.setFilm (filmService.findById (id));

        favoriteFilmCart.getItems ().add (favoriteFilmItem);
        favoriteFilmCart.setSessionToken (sessionToken);
        favoriteFilmCart.setDate (new Date ());
        return favoriteFilmCartRepository.save (favoriteFilmCart);
    }
//     Add Favorite Film
    public FavoriteFilmCart addFavoriteFilm(Long id, String sessionToken) {
        FavoriteFilmCart favoriteFilmCart = favoriteFilmCartRepository.findBySessionToken (sessionToken);
        Film film = filmService.findById (id);
        Boolean filmDoesExistInTheCart =false;
        if (favoriteFilmCart != null) {
            Collection<FavoriteFilmItem> items = favoriteFilmCart.getItems ( );
            for (FavoriteFilmItem item : items) {
                if (item.getFilm ().equals (film)) {
                    filmDoesExistInTheCart =true;
                    favoriteFilmCart.setItems (items);
                    return favoriteFilmCartRepository.saveAndFlush (favoriteFilmCart);
                }
            }
        }
        if (!filmDoesExistInTheCart && (favoriteFilmCart != null)){
            FavoriteFilmItem favoriteFilmItem = new FavoriteFilmItem ();
            favoriteFilmItem.setDate (new Date (  ));
            favoriteFilmItem.setFilm (film);
            favoriteFilmCart.getItems ().add (favoriteFilmItem);
            return favoriteFilmCartRepository.saveAndFlush (favoriteFilmCart);
        }
        return this.addFavoriteFilm (id,sessionToken);
    }
//    Find By Session Token
    public FavoriteFilmCart getFavoriteFilmBySessionToken(String sessionToken) {
        return  favoriteFilmCartRepository.findBySessionToken(sessionToken);
    }
//    Remove Favorite Film By Id
    public FavoriteFilmCart removeFavoriteFilm(Long id, String sessionToken) {
        FavoriteFilmCart favoriteFilmCart = favoriteFilmCartRepository.findBySessionToken(sessionToken);
        Collection<FavoriteFilmItem> items = favoriteFilmCart.getItems ();
        FavoriteFilmItem favoriteFilmItem = null;
        for(FavoriteFilmItem item : items) {
            if(Objects.equals (item.getId ( ), id)) {
                favoriteFilmItem = item;
            }
        }
        items.remove(favoriteFilmItem);
        favoriteFilmItemRepository.delete(favoriteFilmItem);
        favoriteFilmCart.setItems (items);
        return favoriteFilmCartRepository.save(favoriteFilmCart);
    }
//    Clear Favorite Page
    public void deleteAllFavoriteFilm(String sessionToken) {
        FavoriteFilmCart favoriteFilmCart = favoriteFilmCartRepository.findBySessionToken(sessionToken);
        favoriteFilmCartRepository.delete(favoriteFilmCart);
    }
}
