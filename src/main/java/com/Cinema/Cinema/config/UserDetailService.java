package com.Cinema.Cinema.config;

import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@SuppressWarnings( "ALL" )
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail (email);
        if (user == null){
            throw new UsernameNotFoundException ("Invalid E-Mail Or Password");
        }
        return new UserDetail (user);
    }
}
