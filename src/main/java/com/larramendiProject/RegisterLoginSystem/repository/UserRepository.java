package com.larramendiProject.RegisterLoginSystem.repository;

import com.larramendiProject.RegisterLoginSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
