package com.Cinema.Cinema.config;

import com.Cinema.Cinema.oauth2.UserLoginSuccessHandler;
import com.Cinema.Cinema.oauth2.UserOAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings( "ALL" )
@Configuration
@EnableWebSecurity
public class SecurityWebService extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService detailsService(){
        return new UserDetailService ();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder (  );
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider ();
        auth.setPasswordEncoder (passwordEncoder ());
        auth.setUserDetailsService (detailsService ());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider (daoAuthenticationProvider ());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf ().disable ().authorizeRequests (  )
                .antMatchers ("/shoppingCart/**","/Favorite/**").hasAnyAuthority ("Admin","User")
                .antMatchers ("/createFilm/**").hasAuthority ("Admin")
                .antMatchers ("/oauth2/**").permitAll ().antMatchers ("/login").
                authenticated ().anyRequest ().permitAll ().and ()
                .formLogin (  ).loginPage ("/login").usernameParameter ("email").defaultSuccessUrl ("/")
                .permitAll ()
                .and ().
                oauth2Login ().loginPage ("/login").userInfoEndpoint ().userService (oAuth2Service)
                        .and ().successHandler (userLoginSuccessHandler).and ().
                logout (  ).invalidateHttpSession (true).logoutRequestMatcher (new AntPathRequestMatcher ("/logout"))
                .logoutSuccessUrl ("/login?logout").permitAll ().and ().exceptionHandling ().accessDeniedPage ("/403");

    }
    @Autowired
    private UserOAuth2Service oAuth2Service;

    @Autowired
    private UserLoginSuccessHandler userLoginSuccessHandler;
}
