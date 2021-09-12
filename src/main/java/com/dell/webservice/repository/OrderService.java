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
import com.dell.webservice.interfaces.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
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
	
	
	public void addOrder(Order orderObj) {
		this.orderRepository.save(orderObj);
	}
	
	public void updateOrder(Order orderObj) {
		this.orderRepository.save(orderObj);
	}
	
	public void deleteOrder(int id) {
		this.orderRepository.deleteById(id);
	}
	
}
