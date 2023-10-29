package com.Cinema.Cinema.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings( "ALL" )
public class UserOauth2 implements OAuth2User {
      private OAuth2User oAuth2User;
    public UserOauth2(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes ();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<> (  );
        oAuth2User.getAuthorities ().forEach (ga -> authorities.add (ga));
        authorities.add (new SimpleGrantedAuthority ("User"));
       return authorities;
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute ("name");
    }
    public String userNameEmail() {
        return oAuth2User.getAttribute ("name");
    }
    public String getEmail() {
        return oAuth2User.getAttribute ("email");
    }

}
