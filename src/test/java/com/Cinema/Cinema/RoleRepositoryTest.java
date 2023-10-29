package com.Cinema.Cinema;

import com.Cinema.Cinema.model.Role;
import com.Cinema.Cinema.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    RoleRepository repo;


    @Test
    public void testCreateRole(){
        Role user = new Role ("User");
        Role admin = new Role ( "Admin" );

        repo.saveAll (List.of ( user,admin ));

        List<Role> roles =  repo.findAll ();
        assertThat (roles.size ()).isEqualTo (2);

    }
}

