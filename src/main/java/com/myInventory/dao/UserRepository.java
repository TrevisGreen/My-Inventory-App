package com.myInventory.dao;

import com.myInventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public class UserRepository extends JpaRepository<User, String> {
}
