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

import com.dell.webservice.entity.Product;
import com.dell.webservice.exception.InvalidProductException;
import com.dell.webservice.exception.ProductNotFoundException;
import com.dell.webservice.interfaces.ProductRepository;
import com.dell.webservice.repository.ProductService;

@RestController
public class ProductController {
	@Autowired
	ProductService productService;
	

	//get products
		@GetMapping("/products")
		public ResponseEntity<?> getProducts(
				 @RequestParam(defaultValue = "0") Integer pageNo, 
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(defaultValue = "id") String sortBy) {
				try {
					List<Product> products = productService.getAllProduct(pageNo, pageSize, sortBy);
					return new ResponseEntity<List<Product>>(products, new HttpHeaders(), HttpStatus.OK);
				}catch(Exception ex) {
					return new ResponseEntity<String>("Unable to get products", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
		
	//create a product
		@PostMapping("/products")
		public ResponseEntity<?> addProduct(@RequestBody(required = false) Product productObj){
			if(productObj==null)
			{
				return new ResponseEntity<String>("Request body can not be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
			try {
				 this.productService.addProduct(productObj);
				 return new ResponseEntity<List<Product>>( new HttpHeaders(), HttpStatus.CREATED);
			}catch(Exception ex) {
				return new ResponseEntity<String>("Unable to add products", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//update a product
				@PutMapping("/products/{id}")
				public ResponseEntity<?> updateProduct(@PathVariable("id") int id, @RequestBody(required = false) Product productObj){
					if(productObj==null)
					{
						return new ResponseEntity<String>("Request body can not be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
					}
					if(id != productObj.getId()) {   
						return new ResponseEntity<String>("Id in requested path and requested body do not match", new HttpHeaders(), HttpStatus.BAD_REQUEST);
					}
					try {
						Product fetchProduct = this.productService.getProductById(id);
						if(fetchProduct == null)
						{
							return new ResponseEntity<String>("Product does not exist with  id"+id, new HttpHeaders(), HttpStatus.NOT_FOUND);
						}
						 this.productService.updateProduct(productObj);
						 return new ResponseEntity<String>("Product is added successfully!", new HttpHeaders(), HttpStatus.NO_CONTENT);
					}catch(Exception ex) 
					{
						return new ResponseEntity<String>("Unable to add products", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
		//delete product
				@DeleteMapping("/products/{id}")
				public ResponseEntity<?> deleteProduct(@PathVariable("id") int id){
					try {
						Product fetchProduct = this.productService.getProductById(id);
						if(fetchProduct == null) {
							return new ResponseEntity<String>("Product does not exist with id"+id,new HttpHeaders(), HttpStatus.NOT_FOUND);
						}else {
							this.productService.deleteProduct(id);
							return new ResponseEntity<Product>(new HttpHeaders(), HttpStatus.NO_CONTENT);
							
						}
					}catch(Exception ex) {
						return new ResponseEntity<String>("Unable to delete product", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
}