package com.demo.first.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private Map<Integer, User> userDb = new HashMap<>();

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println(user.getEmail());   //gets email in terminal
        userDb.putIfAbsent(user.getId(), user);  //put only if id is absent and we use usergetId() to use to for id in hashmap
//            return ResponseEntity.status(HttpStatus.CREATED).body(user); Or we do use This
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        if (userDb.containsKey(user.getId())) {
            userDb.put(user.getId(), user);
            return ResponseEntity.status(HttpStatus.OK).body("Updated Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteuser(@PathVariable int id) {
        if (userDb.containsKey(id)) {
            userDb.remove(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

    }

//        @GetMapping({"/users","/user/{id}")

    @GetMapping
    public List<User> getUser() {
//            List<User> user11 = new ArrayList<>(userDb.values());
//            return user11; or we can write like this
        return new ArrayList<>(userDb.values());
    }


    //Fetch info of data
    @GetMapping("/{userid}")
    public ResponseEntity<User> getorder(
            @PathVariable(value = "userid", required = false) int id) {
        if (!userDb.containsKey(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(userDb.get(id));
    }

    @GetMapping("/{userid}/orders/{orderId}")
    public ResponseEntity<User> getuserorder(
            @PathVariable("userid") int id, @PathVariable int orderId) {
        System.out.println("Order Id" + orderId);
        if (!userDb.containsKey(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(userDb.get(id));
    }


    // /search?name=john this how this api is getting called
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUser(
        @RequestParam(required = false, defaultValue = "Lily") String name,
        @RequestParam(required = false, defaultValue = "email") String email  //Using multiple Query Parameters
        )
        {
            // we can also set default value in our system
            System.out.println(name);
            List<User> users = userDb.values().stream()
                    .filter(user -> user.getName().equalsIgnoreCase(name))
                    .filter(user -> user.getEmail().equalsIgnoreCase(email))
                    .toList();
//        return ResponseEntity.ok(new ArrayList<>(userDb.values()));  //returning all users
            return ResponseEntity.ok(new ArrayList<>(users));
        }


        //Putting ALl together
        @GetMapping("/info/{id}")
        public String getinfo(
                @PathVariable String id,
                @RequestParam String name,
                @RequestHeader("User-Agent")String userAgent){
            return "User Agent: "+ userAgent
                    +" : "+ id
                    +" : "+name;
        }
    }

    //QUERY paramters are passed in URL and retrived using @RequestParam
