package com.Cinema.Cinema.repository;

import com.Cinema.Cinema.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("Select r From Role r Where r.name =?1")
    public Role findByName(String name);

}
