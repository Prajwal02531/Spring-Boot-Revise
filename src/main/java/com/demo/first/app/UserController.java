package com.demo.first.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService = new UserService();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
       User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updated = userService.updatedUser(user);
        if(updated == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteuser(@PathVariable int id) {
        boolean isDelted = userService.deleteUser(id);
        if (isDelted) {
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
        } else {// else 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

    }


    @GetMapping
    public ResponseEntity<List<User>> getUser() {
        List<User>user = userService.getAllUser();
        if(user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(user); //used to fetch all values
    }

    //Fetch info of data
    @GetMapping("/{userid}")
    public ResponseEntity<User> getorder (@PathVariable(value = "userid", required = false) int id) {
        //we use required = false because if we dont add userid in data then we dont able to fetch & gets error
        User user = userService.getUserById(id);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //if id not contain give error
        return ResponseEntity.ok(user); //if found then return it
    }

    @GetMapping("/{userid}/orders/{orderId}")
    public ResponseEntity<User> getuserorder(
            @PathVariable("userid") int id, @PathVariable int orderId) {  //we can assign multiple pathvariable in single code
        System.out.println("Order Id" + orderId);
        User user = userService.getUserById(id);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //if id not contain give error
        return ResponseEntity.ok(user); //if found then return it
    }


    // /search?name=john this how this api is getting called
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUser(
        @RequestParam(required = false, defaultValue = "Lily") String name,
        @RequestParam(required = false, defaultValue = "email") String email
        //Using multiple Query Parameters to parse name and email in query parameter to search the entity
        )
        {
            List<User> user = userService.searchUsers(name, email);
            return ResponseEntity.ok(user);
        }
                //There is one alternative without streams
            //        {
            //            // we can also set default value in our system
            //            System.out.println(name);
            //            List<User> users = userDb.values().stream()
            //                    .filter(user -> user.getName().equalsIgnoreCase(name))
            //                    .filter(user -> user.getEmail().equalsIgnoreCase(email))
            //                    .toList();
            ////        return ResponseEntity.ok(new ArrayList<>(userDb.values()));  //returning all users
            //            return ResponseEntity.ok(new ArrayList<>(users));
            //        }
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

        //Exception Handling Method
    //Custom Handler
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
        public ResponseEntity<Map<String ,Object>>handlIllegalArgumentException(Exception exception){
            Map<String,Object> errorresponse = new HashMap<>();
            errorresponse.put("Status",HttpStatus.BAD_REQUEST.value());
            errorresponse.put("Timestamp", LocalDateTime.now());
            errorresponse.put("Error", "Bad Request");
            errorresponse.put("Message",exception.getMessage());
            return new ResponseEntity<>(errorresponse,HttpStatus.BAD_REQUEST);
        }
    }


