package com.dell.webservice.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dell.webservice.entity.Product;
import com.dell.webservice.interfaces.ProductRepository;

@Service
public class ProductService {
	@Autowired
	ProductRepository productRepository;
	
	public List<Product> getAllProduct(Integer pageNo, Integer pageSize, String sortBy, String productName){
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		 
        Page<Product> pagedResult = productRepository.findAll(paging);
        
         System.out.println(productName);
         if(productName == null) {
				pagedResult =  productRepository.findAll(paging);
			}
			else {
				pagedResult =  productRepository.findByNameContaining(productName, paging);
			}
         
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Product>();
        }
	}
	
	public Product getProductById(int id) {
		Product fetchProduct = this.productRepository.findById(id).get();
		return fetchProduct;
	}
	
	
	public void addProduct(Product productObj) {
		this.productRepository.save(productObj);
	}
	
	public void updateProduct(Product productObj) {
		this.productRepository.save(productObj);
	}
	
	public void deleteProduct(int id) {
		this.productRepository.deleteById(id);
	}
	
}

