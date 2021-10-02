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

import com.dell.webservice.entity.Order;
import com.dell.webservice.entity.Product;
import com.dell.webservice.entity.User;
import com.dell.webservice.repository.OrderService;
import com.dell.webservice.repository.UserService;

@RestController
public class OrderController {
	//get orders
	@Autowired
	OrderService orderService;
	
	@Autowired
	UserService userService; 

	
			@GetMapping("/orders")
			public ResponseEntity<?> getOrder(
					 @RequestParam(defaultValue = "0") Integer pageNo, 
	                 @RequestParam(defaultValue = "5") Integer pageSize,
	                 @RequestParam(defaultValue = "id") String sortBy) {
					try {
						List<Order> orders = orderService.getAllOrders(pageNo, pageSize, sortBy);
						return new ResponseEntity<List<Order>>(orders, new HttpHeaders(), HttpStatus.OK);
					}catch(Exception ex) {
						return new ResponseEntity<String>("Unable to get orders", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
					}
			}
			
		//create an order
			@PostMapping("/orders")
			public ResponseEntity<?> addOrder(@RequestBody(required = false) Order orderObj){
				if(orderObj==null)
				{
					return new ResponseEntity<String>("Request body can not be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
				}
				try {
						 var userDetails = this.orderService.addOrder(orderObj);
						 return new ResponseEntity<String>( "Total Balance: "+ orderObj.getTotalPrice()+"\n"+"wallet Balance : "+userDetails.getWalletBalance(), new HttpHeaders(), HttpStatus.CREATED);
					
				}catch(Exception ex) {
					return new ResponseEntity<String>("Unable to add orders", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			//update an order
					@PutMapping("/orders/{id}")
					public ResponseEntity<?> updateOrder(@PathVariable("id") int id, @RequestBody(required = false) Order orderObj, @RequestParam(required = false) String userName){
						if(orderObj==null)
						{
							return new ResponseEntity<String>("Request body can not be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
						}
						if(id != orderObj.getId()) {   
							return new ResponseEntity<String>("Id in requested path and requested body do not match", new HttpHeaders(), HttpStatus.BAD_REQUEST);
						}
						try{
							boolean role = userService.checkAdmin(userName);
						 if(role == true) {
							 Order fetchOrder = this.orderService.getOrderById(id);
								if(fetchOrder == null)
								{
									return new ResponseEntity<String>("Order does not exist with  id"+id, new HttpHeaders(), HttpStatus.NOT_FOUND);
								}
								 this.orderService.updateOrder(orderObj);
								 return new ResponseEntity<String>("Order is added successfully!", new HttpHeaders(), HttpStatus.NO_CONTENT);
						
						 }else {
							 return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
						 }
						
								}catch(Exception ex) 
						{
							return new ResponseEntity<String>("Unable to add order", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
						}
					}
					
					//delete an order
					@DeleteMapping("/orders/{id}")
					public ResponseEntity<?> deleteOrder(@PathVariable("id") int id, @RequestParam(required = false) String userName){
						try {
							boolean role = this.userService.checkAdmin(userName);
							if(role == true)
							{
								Order fetchOrder = this.orderService.getOrderById(id);
								if(fetchOrder == null) {
									return new ResponseEntity<String>("Order does not exist with id"+id,new HttpHeaders(),
											 HttpStatus.NOT_FOUND);
								}else{
									this.orderService.deleteOrder(id);
									return new ResponseEntity<Product>(new HttpHeaders(), HttpStatus.NO_CONTENT);
								}
							}
							else {
								
								return new ResponseEntity<String>("Unauthorized Request", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
							}
						}catch(Exception ex) {
							return new ResponseEntity<String>("Unable to delete order", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
						}
					}

}
