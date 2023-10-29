package com.Cinema.Cinema.controller;

import com.Cinema.Cinema.model.Film;
import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.oauth2.Utility;
import com.Cinema.Cinema.repository.UserRepository;
import com.Cinema.Cinema.config.UserNotFoundException;
import com.Cinema.Cinema.service.FilmService;
import com.Cinema.Cinema.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
@SuppressWarnings( "ALL" )
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    public UserController(FilmService filmService) {
        this.filmService = filmService;
    }
    @Autowired
    private JavaMailSender javaMailSender;
// Return To Main Page
    @GetMapping("/return")
    public String returnToMainPage(){
        return "redirect:/main";
    }
//Show Main Page
    @GetMapping("/main")
    public String introduction(@AuthenticationPrincipal UserDetails loggedUser, Model model ,@Param("keyword")String keyword){
     if (loggedUser != null) {
         String email = loggedUser.getUsername ( );
         User user = userRepository.findByEmail (email);
         model.addAttribute ("list", user);
         List<Film> films = filmService.listAll(keyword);
         model.addAttribute ("keyword",keyword);
         model.addAttribute ("films",films);
     }else if (loggedUser == null){
     List<Film> films = filmService.listAll(keyword);
     model.addAttribute ("keyword",keyword);
     model.addAttribute ("films",films);
     }
        return "/main";
    }
//      Registration Process
    @GetMapping("/registration")
    public String registrationPage(@AuthenticationPrincipal UserDetails loggedUser, Model model){
        if (loggedUser != null) {
            String email = loggedUser.getUsername ( );
            User user = userRepository.findByEmail (email);
            model.addAttribute ("list", user);
        }else if (loggedUser == null){
            model.addAttribute ("user",new User (  ));
           return "/registration";
        }
        return "/registration";
    }
//   Create User
    @PostMapping("/saveUser")
    public String save(@RequestParam( "picture" )
                           MultipartFile file,User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String fileName = StringUtils.cleanPath (Objects.requireNonNull (file.getOriginalFilename ( )));
        if (fileName.contains ("..")) {
            System.out.println ("not a a valid file");
        }
        try {
            user.setFile (Base64.getEncoder ( ).encodeToString (file.getBytes ( )));
        } catch (IOException e) {
            e.printStackTrace ( );
        }
          userService.saveUser (user);
          String siteURL = Utility.getSiteURL (request);
          userService.sendVerificationCode (user,siteURL);
          return "/login";
    }
//    Login Success
    @GetMapping("/")
    public String loginSuccess(@AuthenticationPrincipal UserDetails loggedUser, Model model ,@Param("keyword")String keyword){
        String email = loggedUser.getUsername ( );
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list", user);
        List<Film> films = filmService.listAll(keyword);
        model.addAttribute ("keyword",keyword);
        model.addAttribute ("films",films);
        return "/main";
    }
//    Verify Account through E-Mail
    @GetMapping("/verify")
    public String verifyAccount( @Param ("code")String code, Model model){
        boolean verified = userService.verify (code);
        if (verified == false){
        return "fail";
        }
            String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed";
            model.addAttribute ("pageTitle",pageTitle);
        return "login";
    }
//    Login Page
    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal UserDetails loggedUser,Model model){
        if (loggedUser != null) {
            String email = loggedUser.getUsername ( );
            User user = userRepository.findByEmail (email);
            model.addAttribute ("list", user);
            return "login";
        }else if (loggedUser == null){
            return "login";
        }
        return "login";
    }
//    Search Film
    @RequestMapping("/search")
    public String listByPage(@AuthenticationPrincipal UserDetails loggedUser,  Model model, @Param ("keyword")String keyword) {
        if (loggedUser != null) {
            String email = loggedUser.getUsername ( );
            User user = userRepository.findByEmail (email);
            model.addAttribute ("list", user);
            List <Film> films = filmService.listAll (keyword);
            model.addAttribute("films",films);
            model.addAttribute("keyword", keyword);
            return "/main";
        }else if (loggedUser == null){
            List <Film> films = filmService.listAll (keyword);
            model.addAttribute("films",films);
            model.addAttribute("keyword", keyword);
            return "/main";
        }
        return "/main";
    }
//    Show Forgot Password Form
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(@AuthenticationPrincipal UserDetails loggedUser,Model model){
        if (loggedUser != null) {
            String email = loggedUser.getUsername ( );
            User user = userRepository.findByEmail (email);
            model.addAttribute ("list", user);
            model.addAttribute ("pageTitle","ForgotPassword");
        }else if (loggedUser == null){
            model.addAttribute ("pageTitle","ForgotPassword");
        }
        return "forgot_password_form";
    }
//    Send A Resset Passsword Link
    @PostMapping("/forgot_password")
    public String forgotPasswordForm(HttpServletRequest request,Model model){
        String email = request.getParameter ("email");
       String token =  RandomString.make (45);
        try {
            userService.updateResetPassword (token,email);
            String resetPasswordLink = Utility.getSiteURL (request) + "/reset_password?token=" + token;
            System.out.println ( resetPasswordLink );
            sendEmail(email,resetPasswordLink);
            model.addAttribute ("message" ,"We have sent a reset password link to your email.Please Check.");
        } catch (UserNotFoundException ex) {
            model.addAttribute ("error",ex.getMessage ());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute ("error","Error while sending email");
        }
        model.addAttribute ("pageTitle","ForgotPassword");
        return "forgot_password_form";
    }
//    Show Reset Password Form
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param (value = "token")String token, Model model){
        User user = userService.get (token);
        if (user == null){
            model.addAttribute ("title","Change your password");
            model.addAttribute ("message","Invalid Token");
            return "fail";
        }
        model.addAttribute ("token",token);
        model.addAttribute ("pageTitle","Reset Your Password");
        return "reset_password_form";
    }
//    Resset Password Success
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request,Model model){
     String token = request.getParameter ("token");
     String password = request.getParameter ("password");
        User user = userService.get (token);
        if (user == null){
            model.addAttribute ("title","Reset Your Password");
            model.addAttribute ("message","Invalid Token");
            return "/registration";
        }else {
            userService.updatePassword (user,password);
            model.addAttribute ("message","You have successfully changed your password");
        }
        return "/login";
    }
//    Send Message On E-Mail Verification
    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage ();
        MimeMessageHelper helper = new MimeMessageHelper (mimeMailMessage);
        helper.setFrom ("mkoszalka0@gmail.com","Cinema Support");
        helper.setTo (email);
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>" +
                "<p>You have requested to reset your  password.</p>"
                + "<p>Click the link below to change your password:</p>"
                +"<p><a href =\"" + resetPasswordLink +"\">Change my Password</a></p>"
                +"<p>Ignore this email if you do remember your password.</p>";
        helper.setSubject (subject);
        helper.setText (content,true);

        javaMailSender.send (mimeMailMessage);
    }
//    Information About The User
    @GetMapping( "/account" )
    public String viewUser(@AuthenticationPrincipal UserDetails loggedUser, Model model) {
        String email = loggedUser.getUsername ( );
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list", user);
        return "/account";
    }
//   Edit User Profile Page
    @GetMapping("/list/editProfile/{id}")
    public String changeUserProfile(@AuthenticationPrincipal UserDetails loggedUser,@PathVariable Long id,Model model){
        model.addAttribute ("user",userRepository.getById (id));
        String email = loggedUser.getUsername ( );
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list", user);
        return "/editProfile";
    }
//    Change User Profile
    @PostMapping( "/profile/{id}" )
    public String saveNewProfile(@AuthenticationPrincipal UserDetails loggedUser, @PathVariable Long id, @ModelAttribute( "user" ) User user, Model model,
                                  @RequestParam( "picture" ) MultipartFile file) {

        String fileName = StringUtils.cleanPath (Objects.requireNonNull (file.getOriginalFilename ( )));
        if (fileName.contains ("..")) {
            System.out.println ("not a a valid file");
        }
        try {
            user.setFile (Base64.getEncoder ( ).encodeToString (file.getBytes ( )));
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        User setUser = userRepository.getReferenceById (id);
        setUser.setId (id);
        setUser.setFile (user.getFile ( ));
        userRepository.save (setUser);
        return "login";
    }
    //   Edit User UserName Page
    @GetMapping("/list/editUsername/{id}")
    public String changeUserUserName(@AuthenticationPrincipal UserDetails loggedUser,@PathVariable Long id,Model model){
        String email = loggedUser.getUsername ( );
        User user = userRepository.findByEmail (email);
        model.addAttribute ("list", user);
        model.addAttribute ("user",userRepository.getById (id));
        return "editUsername";
    }
    //    Change User UserName
    @PostMapping( "/user/{id}" )
    public String saveNewUserName(@AuthenticationPrincipal UserDetails loggedUser, @PathVariable Long id, @ModelAttribute( "user" ) User user, Model model) {
        User setUser = userRepository.getReferenceById (id);
        setUser.setId (id);
        setUser.setName (user.getName ());
        userRepository.save (setUser);
        return "login";
    }
//    Drop Account
    @GetMapping("/removeAccount/{id}")
    public String removeAccount(@PathVariable("id") Long id ) {
        userService.removeAccount (id);
        return "/login";
    }
//    Lack Of Access
    @GetMapping("/403")
    private String lackOfAccess(@AuthenticationPrincipal UserDetails loggedUser,Model model){
        if (loggedUser != null) {
            String email = loggedUser.getUsername ( );
            User user = userRepository.findByEmail (email);
            model.addAttribute ("list", user);
            return "/403";
        }else if (loggedUser == null){
            return "/403";
        }
        return "/403";
    }
}
