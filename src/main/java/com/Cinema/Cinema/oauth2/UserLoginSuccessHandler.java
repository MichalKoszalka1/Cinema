package com.Cinema.Cinema.oauth2;

import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.repository.UserRepository;
import com.Cinema.Cinema.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@SuppressWarnings( "ALL" )
@Component
public class UserLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(  HttpServletRequest request, HttpServletResponse response
                                          , Authentication authentication)
            throws ServletException, IOException {
        UserOauth2 userOauth2 = (UserOauth2) authentication.getPrincipal ( );
        String email = userOauth2.getEmail ( );
        String name = userOauth2.getName ();
        User user = userService.getUserBYEmail (email);

        if (user == null) {
//            Create new User
            userService.createNewUserGoogle (email, name, AuthenticationProvider.GOOGLE);
        } else {
//            update User
            userService.updateUserGoogle (user, name, AuthenticationProvider.GOOGLE);
        }
        super.onAuthenticationSuccess (request, response, authentication);
    }
}
