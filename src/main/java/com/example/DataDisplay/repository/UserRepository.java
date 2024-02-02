package com.example.DataDisplay.repository;

import com.example.DataDisplay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
