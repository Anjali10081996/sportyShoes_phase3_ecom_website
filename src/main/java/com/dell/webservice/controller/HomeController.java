package com.dell.webservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	@GetMapping("/")
	public String indexMapping() {
		return "Welcome to Sporty shoes Website";
	}
}
