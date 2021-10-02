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
import com.dell.webservice.repository.ProductService;
import com.dell.webservice.repository.UserService;

@RestController
public class ProductController {
	@Autowired
	ProductService productService;
	@Autowired
	UserService userService;
	

	//get products
		@GetMapping("/products")
		public ResponseEntity<?> getProducts(
				 @RequestParam(defaultValue = "0") Integer pageNo, 
                 @RequestParam(defaultValue = "5") Integer pageSize,
                 @RequestParam(defaultValue = "id") String sortBy,
                 @RequestParam(required = false) String name) {
				try {
					List<Product> products = productService.getAllProduct(pageNo, pageSize, sortBy,name);
					return new ResponseEntity<List<Product>>(products, new HttpHeaders(), HttpStatus.OK);
				}catch(Exception ex) {
					return new ResponseEntity<String>("Unable to get products", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
		
		@GetMapping("/products/{id}")
		public ResponseEntity<?> getProductById(@PathVariable("id") int id, @RequestParam(required = false) String userName){
			try {
				boolean role= this.userService.checkAdmin(userName);
				if(role == true) {
					Product product = this.productService.getProductById(id);
					if(product==null) {
						return new ResponseEntity<String>("Product does not exist with id"+id, new HttpHeaders(), HttpStatus.NOT_FOUND);
					}else {
						return new ResponseEntity<Product>(product, new HttpHeaders(), HttpStatus.OK);
					}
				}else {
					return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
				}
			}catch(Exception ex){
				System.out.println(ex.getMessage().toString());
				return new ResponseEntity<String>("Unable to fetch products",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	//create a product
		@PostMapping("/products")
		public ResponseEntity<?> addProduct(@RequestBody(required = false) Product productObj, @RequestParam(required = false) String userName){
			if(productObj==null)
			{
				return new ResponseEntity<String>("Request body can not be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
			try {
		
				boolean role = userService.checkAdmin(userName);
				if(role == true) {
					this.productService.addProduct(productObj);
					return new ResponseEntity<Product>(productObj, new HttpHeaders(), HttpStatus.CREATED);
				}
				else {
					return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
				}
			}catch(Exception ex) {
				return new ResponseEntity<String>("Unable to add products", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//update a product
				@PutMapping("/products/{id}")
				public ResponseEntity<?> updateProduct(@PathVariable("id") int id, @RequestBody(required = false) Product productObj,@RequestParam(required = false) String userName){
					if(productObj==null)
					{
						return new ResponseEntity<String>("Request body can not be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
					}
					if(id != productObj.getId()) {   
						return new ResponseEntity<String>("Id in requested path and requested body do not match", new HttpHeaders(), HttpStatus.BAD_REQUEST);
					}
					try {
						boolean role = userService.checkAdmin(userName);
						if(role == true) {
							Product fetchProduct = this.productService.getProductById(id);
							if(fetchProduct == null) {
								return new ResponseEntity<String>("Product does not exist with id " + id,new HttpHeaders(), HttpStatus.NOT_FOUND);
							}
							this.productService.updateProduct(productObj);
							return new ResponseEntity<String>("Product is updated successfully",new HttpHeaders(), HttpStatus.OK);
						}
						else {
							return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
						}
					}catch(Exception ex) 
					{
						return new ResponseEntity<String>("Unable to add products", new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
		//delete product
				@DeleteMapping("/products/{id}")
				public ResponseEntity<?> deleteProduct(@PathVariable("id") int id,@RequestParam(required = false) String userName){
					try {
						boolean role = userService.checkAdmin(userName);
						if(role == true) {
							Product fetchProduct = this.productService.getProductById(id);
							if(fetchProduct == null) {
								return new ResponseEntity<String>("Product does not exist with id"+id,new HttpHeaders(), HttpStatus.NOT_FOUND);
						}else {
							this.productService.deleteProduct(id);
							return new ResponseEntity<Product>(new HttpHeaders(), HttpStatus.NO_CONTENT);
							
						}
					}else {
						return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
						}
					}
					catch(Exception ex) {
						return new ResponseEntity<String>("Unable to delete product", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

						}
					
				}
}