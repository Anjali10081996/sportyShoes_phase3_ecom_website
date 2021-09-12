package com.dell.webservice.interfaces;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.dell.webservice.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer>{

}
