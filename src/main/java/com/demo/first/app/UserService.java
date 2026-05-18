package com.demo.first.app;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private Map<Integer, User> userDb = new HashMap<>();

    public User createUser(User user) {
        System.out.println(user.getEmail());
        userDb.putIfAbsent(user.getId(), user);
        return user;
    }

    public User updatedUser(User user) {
        if (userDb.containsKey(user.getId())) {
            userDb.put(user.getId(), user);
            return user;
        } else {
            throw new IllegalArgumentException("User With Id:- "+user.getId()+"Does not exit");
        }

    }

    public boolean deleteUser(int id) {
        if (userDb.containsKey(id)) {
            userDb.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public List<User> getAllUser() {
        if(userDb.values().isEmpty())
            throw new NullPointerException("No users found in database");
        return new ArrayList<>(userDb.values());

    }

    public User getUserById(int id) {
        if(!userDb.containsKey(id))
            return null;

        return userDb.get(id);
    }

    public List<User> searchUsers(String name, String email) {

        List<User> users = new ArrayList<>();
        for(User user : userDb.values()){
            if (user.getName().equalsIgnoreCase(name) && user.getEmail().equalsIgnoreCase(email)) {

                users.add(user);
            }
        }
        return users;
    }

}
