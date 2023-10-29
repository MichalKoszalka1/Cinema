package com.Cinema.Cinema;

import com.Cinema.Cinema.model.Role;
import com.Cinema.Cinema.model.User;
import com.Cinema.Cinema.repository.RoleRepository;
import com.Cinema.Cinema.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings( "ALL" )
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void TryToAddRoleTOUser(){
        User user = new User (  );
        user.setName ("Michał");
        user.setEmail ("mkoszalka0@gmail.com");
        user.setPassword ("Michał");
        user.setEnabled (true);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder (  );
        String password = passwordEncoder.encode (user.getPassword ( ) );
        user.setPassword (password);

       Role role =  roleRepository.findByName ("Admin");

       user.addRole (role);
       User savedUser = userRepository.save (user);
       assertThat(savedUser.getRoles ().size ()).isEqualTo (1);


    }
    @SuppressWarnings( "OptionalGetWithoutIsPresent" )
    @Test
    public void  testAddRoleToExistingUser(){
        User user = userRepository.findById(1L).get();
        Role roleUser = roleRepository.findByName("Admin");
        Role roleCustomer = new Role(2L);

        user.addRole(roleUser);
        user.addRole(roleCustomer);

        User savedUser = userRepository.save (user);

        assertThat(savedUser.getRoles().size()).isEqualTo(2);
    }
}
