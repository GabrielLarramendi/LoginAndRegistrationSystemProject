package com.larramendiProject.RegisterLoginSystem.repository;

import com.larramendiProject.RegisterLoginSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
