package com.myInventory.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    // Connects to the userDao class
    @Autowired
    private UserRepository repo;

    /**
     * method that calls the method 'getUser' in the userDao class
     * and returns the user's username/email
     *
     * @param username
     * @return UserDetails
     */

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repo.findOne(username);
    }
}
