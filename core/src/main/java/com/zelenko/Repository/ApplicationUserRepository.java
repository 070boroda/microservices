package com.zelenko.Repository;


import com.zelenko.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository <ApplicationUser,Long> {

    public ApplicationUser findByUserName(String userName);
}
