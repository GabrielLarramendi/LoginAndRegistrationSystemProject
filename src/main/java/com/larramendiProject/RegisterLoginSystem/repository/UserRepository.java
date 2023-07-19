package com.larramendiProject.RegisterLoginSystem.repository;

import com.larramendiProject.RegisterLoginSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmailAndPassword(String email, String password);
    User findByEmail(String email);
}
