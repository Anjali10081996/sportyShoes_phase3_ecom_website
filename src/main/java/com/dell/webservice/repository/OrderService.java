package com.dell.webservice.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dell.webservice.entity.Order;
import com.dell.webservice.entity.Product;
import com.dell.webservice.entity.User;
import com.dell.webservice.interfaces.OrderRepository;
import com.dell.webservice.interfaces.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserRepository userRepository;
	
	//get all orders
	public List<Order> getAllOrders(Integer pageNo, Integer pageSize, String sortBy){
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		 
        Page<Order> pagedResult = orderRepository.findAll(paging);
         
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Order>();
        }
	}
	//get order by id
	public Order getOrderById(int id) {
		Order fetchOrder = this.orderRepository.findById(id).get();
		return fetchOrder;
	}
	
	
	public void addOrder(Order orderObj) throws Exception {
		int cflag =0;
		double total =0.0;
		for(Product prod : orderObj.getProducts()) {
			System.out.println(prod.getPrice());
			total = total+prod.getPrice();
		}
		System.out.println("Total Price = "+ total);
		orderObj.setTotalPrice(total);
		
		this.orderRepository.save(orderObj);
		Iterable<User> users = this.userRepository.findAll();
		  for (User u : users) {
			  if(u.getUsername().equals(orderObj.getName())) {
				  System.out.println("Wallet balance");
				  System.out.println(u.getWalletBalance());
				  if(total <= u.getWalletBalance()) {
					  double balance = u.getWalletBalance() - total;
					  u.setWalletBalance(balance);
					  this.userRepository.save(u);
					  cflag = 1;
					  break;
				  }
				  else {
					  throw new Exception("Insufficient balance for user: "+orderObj.getName());
				  }
			  }
		  }
		  if(cflag ==0) {
			  throw new Exception("Customer not found");
		  }
	}
	
	public void updateOrder(Order orderObj) {
		this.orderRepository.save(orderObj);
	}
	
	public void deleteOrder(int id) {
		this.orderRepository.deleteById(id);
	}
	
}
