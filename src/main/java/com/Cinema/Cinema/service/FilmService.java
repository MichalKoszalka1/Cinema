package com.Cinema.Cinema.service;

import com.Cinema.Cinema.model.Film;
import com.Cinema.Cinema.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
@SuppressWarnings( "ALL" )
@Service
@Transactional
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;
//    Create Film
    public void saveFilmToDataBase(MultipartFile file,String title,int ticket, int duration,String category, String description, Double price, String spot, java.sql.Date date, String time,  int age){
        Film f = new Film ();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if(fileName.contains(".."))
        {
            System.out.println("not a a valid file");
        }
        try {
            f.setImage(Base64.getEncoder ().encodeToString (file.getBytes ()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        f.setTitle (title);
        f.setTicket (ticket);
        f.setDuration (duration);
        f.setCategory (category);
        f.setPrice (price);
        f.setSpot (spot);
        f.setDate (date);
        f.setTime (time);
        f.setDescription (description);
        f.setAge (age);
        filmRepository.save (f);
    }
//    Find All Films By Key Word
    public List<Film> listAll(String keyword){
      if (keyword != null){
          return filmRepository.findAll ( keyword);
      }
        return filmRepository.findAll ();
    }
    public Film findById(Long id) {

        return filmRepository.findById(id).get();
    }
    public void removeAccount(Long id) {
        filmRepository.deleteById (id);
    }
}
