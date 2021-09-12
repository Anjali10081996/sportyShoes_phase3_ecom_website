package com.dell.webservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dell.webservice.entity.User;
import com.dell.webservice.repository.UserService;

@RestController
public class UserController {
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody(required=false) User userlogin){
		boolean user = this.userService.loginUser(userlogin);
		if(user ==true) {
			return new ResponseEntity<String>("{\"loggedIn\" : true}",new HttpHeaders(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("{\"error\" : \"Unauthorized\",\"message\" : \"Access denied\"}",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/logout/{username}")
	public ResponseEntity<String> LogoutUser(@PathVariable("username") String userlogout){
		boolean user = this.userService.logoutUser(userlogout);
		if(user == true) {
			return new ResponseEntity<String>("{\"loggedOut\" : true}",new HttpHeaders(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("{\"error\" : \"Unauthorized\",\"message\" : \"Access denied\"}",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy){
		try {
			List<User> userList = userService.getUser(pageNo, pageSize, sortBy);
			return new ResponseEntity<List<User>>(userList, new HttpHeaders(), HttpStatus.OK); 
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to fetch users", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	@GetMapping("/users/{Id}")
	public ResponseEntity<?> getUser(@PathVariable("Id") int id) {
		try {
			User user = this.userService.getUserById(id);
			if(user == null) {
				return new ResponseEntity<String>("User does not exist with id " + id, new HttpHeaders(), HttpStatus.NOT_FOUND); 
			}
			else {
				return new ResponseEntity<User>(user,new HttpHeaders(), HttpStatus.OK);
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage().toString());
			return new ResponseEntity<String>("Unable to fetch users",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> addUser(@RequestBody(required = false) User addUser){
		if(addUser == null) {
			return new ResponseEntity<String>("Add User request body cannot be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		try {
			this.userService.addUser(addUser);
			return new ResponseEntity<User>(addUser, new HttpHeaders(), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<String>("Unable to add users",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("/users/{Id}")
	public ResponseEntity<?> updateUser(@PathVariable("Id") int id, @RequestBody(required = false) User updateUser) {
		if(updateUser == null) {
			return new ResponseEntity<String>("Update User request body cannot be empty",new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		if(id != updateUser.getId()) {
			return new ResponseEntity<String>("Id in request path and request body do not match",new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		try {
			User getUser = this.userService.getUserById(id);
			if(getUser==null) {
				return new ResponseEntity<String>("User does not exist with id " + id,new HttpHeaders(), HttpStatus.NOT_FOUND);
			}
			this.userService.updateUser(updateUser);
			return new ResponseEntity<User>(new HttpHeaders(), HttpStatus.NO_CONTENT);
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to update users",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<String> deleteProduct(@PathVariable("userId") int id){
		try {
			User getUser = this.userService.getUserById(id);
			if(getUser == null) {
				return new ResponseEntity<String>("User does not exist with id " + id,new HttpHeaders(), HttpStatus.NOT_FOUND);
			}
			else {
				this.userService.deleteUser(id);
				return new ResponseEntity<String>("user deleted successfully", new HttpHeaders(), HttpStatus.NO_CONTENT);
			}
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to delete users",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
}
