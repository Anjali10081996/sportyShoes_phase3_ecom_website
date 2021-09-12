package com.dell.webservice.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dell.webservice.entity.User;
import com.dell.webservice.interfaces.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public boolean loginUser(User loginUser) {
		Iterable<User> user = this.userRepository.findAll();
		int flag = 0;
		for(User u : user) {
		   if(u.getUsername().equals(loginUser.getUsername()) && u.getPassword().equals(loginUser.getPassword())) {
			   u.setLoggedIn(true);
			   this.userRepository.save(u);
			   flag = 1;
			   break;
		   }
		}
		return flag == 1;
	}
	
	public boolean logoutUser(String userlogout) {
		Iterable<User> user = this.userRepository.findAll();
		int flag =0;
		for(User u: user) {
			if(u.getUsername().equals(userlogout)) {
				if(u.isLoggedIn() == true) {
					u.setLoggedIn(false);
					flag = 1;
				}
			}
		}
			return flag==1;
	}
	
	public List<User> getUser(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<User> pageResult = userRepository.findAll(paging);
		if(pageResult.hasContent()) {
			return pageResult.getContent();
		}else {
			return new ArrayList<User>();
		}
	}
	
	public User getUserById(int id){
	 User user = this.userRepository.findById(id).get();
	 return user;
	}
	
	public void addUser(User user) {
		this.userRepository.save(user);
	}
	
	public void updateUser(User user) {
		this.userRepository.save(user);
	}
	
	public void deleteUser(int id) {
		this.userRepository.deleteById(id);
	}
}
