package com.Cinema.Cinema.model;

import com.Cinema.Cinema.oauth2.AuthenticationProvider;
import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings( "ALL" )
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email",unique = true)
    private String email;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String file;
    @Column(name = "password")
    private String password;
    @Column(name = "reset_password_token")
    private String resetPasswordToken;
    @Column(name = "verification_code",updatable = false)
    private String verificationCode;
    @Column(name = "enabled")
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authenticationProvider;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",joinColumns = @JoinColumn(name = "user_id" ),inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<> (  );

    public User(){
    }
    public User(String name, String email, String password, String verificationCode,
                boolean enabled, AuthenticationProvider authenticationProvider) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
        this.authenticationProvider = authenticationProvider;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public String getVerifiationCode() {
        return verificationCode;
    }
    public void setVerifiationCode(String verifiationCode) {
        this.verificationCode = verifiationCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roleSet) {
        this.roles = roleSet;
    }
    public void addRole(Role role){
        this.roles.add (role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", resetPasswordToken='" + resetPasswordToken + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", enabled=" + enabled +
                ", authenticationProvider=" + authenticationProvider +
                ", roles=" + roles +
                '}';
    }
}
