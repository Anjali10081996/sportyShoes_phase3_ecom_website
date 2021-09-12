package com.dell.webservice.interfaces;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dell.webservice.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
	Page<Product> findByNameContaining(String name, Pageable pageable);
}
