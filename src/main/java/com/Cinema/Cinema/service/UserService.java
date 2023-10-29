package com.Cinema.Cinema.service;

import com.Cinema.Cinema.config.UserNotFoundException;
import com.Cinema.Cinema.model.Role;
import com.Cinema.Cinema.oauth2.AuthenticationProvider;
import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.repository.RoleRepository;
import com.Cinema.Cinema.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;
@SuppressWarnings( "ALL" )
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository  userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JavaMailSender javaMailSender;
//    Get User By Id
    public User getUserById(Long id){
       return userRepository.findById (id).get ();
    }
//    Find User By E-Mail
    public User getUserBYEmail(String email){
        return userRepository.findByEmail (email);
    }
//    Login With Google
    public void createNewUserGoogle( String email, String name, AuthenticationProvider provider) {
        User user = new User ();
        user.setEmail (email);
        user.setName (name);
        user.setEnabled (false);
        user.setAuthenticationProvider (provider);
        userRepository.save (user);
    }
//    Login With Google With Existing Account
    public void updateUserGoogle(User user, String name,
  AuthenticationProvider provider) {
        user.setName (name);
        user.setEnabled (true);
        user.setAuthenticationProvider (provider);
        userRepository.save (user);
    }
//    Registration Proccess
    public void saveUser( User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder (  );
        String password = passwordEncoder.encode (user.getPassword ( ) );
        user.setPassword (password);

        Role role = roleRepository.findByName ("User");
        user.addRole (role);

        String randomCode =  RandomString.make (64);
        user.setVerifiationCode (randomCode);

        user.setEnabled (false);
        userRepository.save (user);
    }
//    Send Verification Code
    public void sendVerificationCode(User user, String siteURL) throws UnsupportedEncodingException ,MessagingException{
        String subject = "Verify Your Account";
        String senderName = "Cinema-Team";
        String mailContent = "<p>Dear " + user.getName () + ",</p>";

        mailContent += "<p>Please Clint Link Below To Verify Your Account:</p>";

        String verifyURL = siteURL + "/verify?code="  + user.getVerifiationCode ();

        mailContent += "<h3><a href=\"" + verifyURL + "\">Verify</a></h3>";

        mailContent += "<p> Cinema Team </p>";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage ();
        MimeMessageHelper helper = new MimeMessageHelper (mimeMessage);

        helper.setFrom ("mkoszalka0@gmail.com ", senderName);
        helper.setTo (user.getEmail ());
        helper.setSubject (subject);
        helper.setText (mailContent,true);

        javaMailSender.send (mimeMessage);
    }
//   Verify Link
    public boolean verify(String verificationCode){
        User user = userRepository.findByVerificationCode (verificationCode);

      if (user == null || user.isEnabled ()){
          return false;
      }else {
          userRepository.enable (user.getId ());
          return true;
      }
    }
//    Change Password
    public void updateResetPassword(String token,String email) throws UserNotFoundException {
        User user = userRepository.findByEmail (email);

        if (user != null){
            user.setResetPasswordToken (token);
            userRepository.save (user);
        }else {
            throw new UserNotFoundException ("Could not find any user with email" + email);
        }
    }
//    Reset Password
    public User get(String resetPasswordToken){
        return userRepository.findByResetPasswordToken (resetPasswordToken);
    }
//    Set New Password
    public void  updatePassword(User user,String newPassword){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder (  );
        String encodePassword= bCryptPasswordEncoder.encode ( newPassword);
        user.setPassword (encodePassword);
        user.setResetPasswordToken (null);
        userRepository.save (user);
    }
//    Find All Users
    public List<User> listAll() {
        return userRepository.findAll();
    }
//    Remove Account By Id
    public void removeAccount(Long id) {
        userRepository.deleteById (id);
    }
}

